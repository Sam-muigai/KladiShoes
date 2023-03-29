package com.sam.kladishoes.presentation.shoelistscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sam.kladishoes.R
import com.sam.kladishoes.model.Response
import com.sam.kladishoes.model.ShoeDetails
import com.sam.kladishoes.presentation.components.CustomButton
import com.sam.kladishoes.presentation.components.LoadingScreen
import com.sam.kladishoes.presentation.components.ShoeItem
import com.sam.kladishoes.presentation.components.TitleSection
import com.sam.kladishoes.ui.theme.yusei

@Composable
fun ShoeListScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoeListViewModel = hiltViewModel(),
    onCartClicked: () -> Unit,
    signOut: () -> Unit,
    onAccountClicked: () -> Unit,
    onOrderClicked: () -> Unit,
    onShoeClicked: (String) -> Unit
) {
    val shoes = viewModel.shoes.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()

    when (val response = viewModel.signOutResponse.value) {
        is Response.Success -> {
            if (response.data) {
                signOut()
            } else {
                ShoeListScreen(
                    scaffoldState,
                    modifier,
                    onAccountClicked,
                    shoes.value,
                    onShoeClicked,
                    onCartClicked,
                    onOrderClicked
                )
            }
        }
        is Response.Loading -> {
            LoadingScreen()
        }
        is Response.Failure -> {
            ShoeListScreen(
                scaffoldState,
                modifier,
                onAccountClicked,
                shoes.value,
                onShoeClicked,
                onCartClicked,
                onOrderClicked
            )
        }
    }
}

@Composable
private fun ShoeListScreen(
    scaffoldState: ScaffoldState,
    modifier: Modifier,
    onAccountClicked:()->Unit,
    shoes: List<ShoeDetails>,
    onShoeClicked: (String) -> Unit,
    onCartClicked: () -> Unit,
    onOrderClicked: () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        Column(
            modifier = modifier.padding(it)
        ) {
            TitleSection(
                leadingIcon = Icons.Default.Person,
                onLeadingIconClicked = {
                  onAccountClicked()
                },
                onTrailingIconClicked = { },
                trailingIcon = {
                    Row {
                        Icon(
                            modifier = Modifier
                                .size(25.dp)
                                .clickable { onOrderClicked() },
                            painter = painterResource(id = R.drawable.order),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            modifier = Modifier
                                .size(25.dp)
                                .clickable { onCartClicked() },
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = null
                        )
                    }
                }
            )
            if (shoes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                      //  .background(Color.Transparent)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            } else {
                val randomIndex = rememberSaveable {
                    mutableStateOf((shoes.indices).random())
                }
                val randomShoe = shoes[randomIndex.value]
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        ImageBanner(imageUrl = randomShoe.imageUrl, shoeName = randomShoe.shoeName){
                            onShoeClicked(randomShoe.id)
                        }
                    }
                    items(shoes) { shoe ->
                        ShoeItem(
                            shoeName = shoe.shoeName,
                            category = shoe.category,
                            price = shoe.price,
                            imageUrl = shoe.imageUrl
                        ) {
                            onShoeClicked(shoe.id)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ImageBanner(
    modifier: Modifier = Modifier,
    shoeName: String,
    imageUrl: String,
    onClick:() ->Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(4.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(100)
                    .build(),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Shoe Image",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(
                    Color.Gray.copy(
                        alpha = 0.6f
                    ),
                    blendMode = BlendMode.Darken
                )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Popular Now",
                    style = MaterialTheme.typography.body1.copy(
                        fontFamily = yusei,
                        fontWeight = FontWeight.ExtraLight
                    ),
                    color = Color.White
                )
                Text(
                    text = shoeName,
                    style = MaterialTheme.typography.h5.copy(
                        fontFamily = yusei
                    ),
                    color = Color.White
                )
            }
            CustomButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp),
                text = "Buy Now",
                shape = RoundedCornerShape(12.dp),
                onClick = onClick
            )
        }
    }
}












