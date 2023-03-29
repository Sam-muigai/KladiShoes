package com.sam.kladishoes.presentation.shoedetailscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import com.sam.kladishoes.R
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sam.kladishoes.model.Response
import com.sam.kladishoes.presentation.components.*
import com.sam.kladishoes.services.AddToCartResponse
import com.sam.kladishoes.ui.theme.yusei
import com.sam.kladishoes.utils.UiEvents
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ShoeDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoeDetailsViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val response = viewModel.addToCartResponse.value
    ShoeDetailScreen(
        onBackClicked = onBackClicked,
        response = response,
        scaffoldState = scaffoldState,
        viewModel = viewModel
    )
}

@Composable
fun ShoeDetailScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    response: AddToCartResponse,
    scaffoldState: ScaffoldState,
    viewModel: ShoeDetailsViewModel
) {
    val listState = rememberLazyListState()
    val shoe = viewModel.clickedShoe.value
    val context = LocalContext.current
    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Top,
                state = listState
            ) {
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        ShoeImage(imageUrl = shoe.imageUrl)
                        FloatingActionButton(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .size(50.dp)
                                .padding(start = 8.dp, top = 8.dp),
                            onClick = { onBackClicked() },
                            backgroundColor = MaterialTheme.colors.background,
                            elevation = FloatingActionButtonDefaults
                                .elevation(defaultElevation = 0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Navigate Back"
                            )
                        }
                    }
                }
                item {
                    ShoeDetails(
                        shoeName = shoe.shoeName,
                        shoeDescription = shoe.description,
                        shoePrice = shoe.price,
                        shoeSize = shoe.size,
                        available = shoe.available
                    )
                }
                item {
                    ReviewItem()
                }
            }
            when (response) {
                is Response.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.BottomCenter))
                }
                is Response.Success -> {
                    if (response.data) {
                        CustomButton(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            shape = MaterialTheme.shapes.small
                        ) {
                            viewModel.addToCart()
                        }
                        showMessage(context,"Added to Cart successfully.")
                    } else {
                        CustomButton(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            shape = MaterialTheme.shapes.small
                        ) {
                            viewModel.addToCart()
                        }
                    }
                }
                is Response.Failure -> {
                    CustomButton(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        shape = MaterialTheme.shapes.small
                    ) {
                        viewModel.addToCart()
                    }
                    showMessage(context, response.e.message!!)
                }
            }
        }
    }
}


@Composable
fun ShoeImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .height(380.dp),
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(500)
            .build(),
        contentDescription = "Shoe Picture",
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ShoeDetails(
    shoeName: String = "Air Jordans",
    shoePrice: Double = 0.0,
    shoeDescription: String = "Awesome Shoe.Check them out",
    shoeSize: String = "31-40",
    material: String = "Leather",
    available: Boolean = true,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
    ) {
        Label(
            text = shoeName,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Label(
            text = "Ksh $shoePrice",
            style = MaterialTheme.typography.body1
        )
        Label(
            text = shoeDescription,
            style = MaterialTheme.typography.body2.copy(
                fontFamily = yusei,
                fontSize = 13.sp
            )
        )
        Label(
            text = "Shoe Size : $shoeSize",
            style = MaterialTheme.typography.body2.copy(
                fontFamily = yusei,
                fontSize = 13.sp
            )
        )
        Label(
            text = "Material: $material",
            style = MaterialTheme.typography.body2.copy(
                fontFamily = yusei,
                fontSize = 13.sp
            )
        )
        Label(
            text = if (available) "Available" else "Stock is finished",
            style = MaterialTheme.typography.body2.copy(
                fontFamily = yusei,
                fontSize = 13.sp
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Yellow,
    text: String = "Add to Cart",
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = modifier,
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = color
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = text,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}


data class Reviews(
    val userName: String,
    val comment: String,
    val imgUrl: String
)

val comments = listOf(
    Reviews(
        "Taylor Swift", "Good shoes ,Great Quality," +
                "I bought this for my brother" +
                " and he liked it alot.",
        "https://thumbs.dreamstime.com/b/happy-person-portrait-smiling-woman-tanned-skin-curly-hair-happy-person-portrait-smiling-young-friendly-woman-197501184.jpg"
    ),
    Reviews(
        "Peter Lackner", "Fast Delivery ,Great Services," +
                "I bought this and they delivered fast" +
                " I liked their services alot.",
        "https://images.pexels.com/photos/1222271/pexels-photo-1222271.jpeg?auto=compress&cs=tinysrgb&w=600"
    )
)


@Composable
fun ReviewItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Category(title = "Reviews")
        comments.forEach {
            Profile(
                data = it.imgUrl,
                profileName = it.userName,
                comment = it.comment
            )
            Divider()
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun Profile(
    modifier: Modifier = Modifier,
    data: String,
    profileName: String,
    comment: String
) {
    Row(modifier = modifier.padding(bottom = 4.dp, top = 4.dp)) {
        LabelImage(data = data)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Label(
                text = profileName,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Label(
                text = comment,
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 14.sp
                )
            )
        }
    }
}



