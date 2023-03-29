package com.sam.kladishoes.presentation.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sam.kladishoes.R
import com.sam.kladishoes.ui.theme.Orange
import com.sam.kladishoes.ui.theme.yusei

@Composable
fun Label(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle
) {
    Text(
        modifier = modifier.padding(top = 4.dp),
        text = text,
        style = style,
    )
}

@Composable
fun Category(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            modifier = Modifier,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Scroll Right"
        )
    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    text: String = "KladiShoes",
    trailingIcon: @Composable () -> Unit,
    leadingIcon: ImageVector? = null,
    onTrailingIconClicked: () -> Unit = {},
    onLeadingIconClicked: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.primarySurface,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement = if (leadingIcon == null) Arrangement.End else Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    modifier = Modifier.clickable { onLeadingIconClicked() },
                    imageVector = leadingIcon,
                    contentDescription = text
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            if (leadingIcon == null) {
                Spacer(modifier = Modifier.width(120.dp))
            }
            trailingIcon()
        }
    }
}

@Composable
fun LabelImage(
    modifier: Modifier = Modifier,
    data: String = "https://thumbs.dreamstime.com/b/happy-person-portrait-smiling-woman-tanned-skin-curly-hair-happy-person-portrait-smiling-young-friendly-woman-197501184.jpg",
    shape: Shape = CircleShape
) {
    AsyncImage(
        modifier = modifier
            .size(65.dp)
            .clip(shape),
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(data)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}


@Composable
fun ShoeItem(
    modifier: Modifier = Modifier,
    shoeName: String,
    category: String,
    price: Double,
    height: Dp = 180.dp,
    imageUrl: String,
    onClick: () -> Unit
) {
    val size = 120.dp
    Card(
        modifier = modifier
            .width(size)
            .padding(4.dp)
            .clickable { onClick() },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(100)
                        .build(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height),
                    contentDescription = "Shoe Image",
                    contentScale = ContentScale.Crop
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.TopStart)) {
                    Label(
                        text = shoeName,
                        style = MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Label(
                        text = category,
                        style = MaterialTheme.typography.caption
                    )
                }
                Label(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    text = "Ksh $price",
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}



@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    text: String = "ADD TO CART",
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        enabled = enabled,
        modifier = modifier,
        onClick = { onClick() },
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Orange
        )
    ) {
        Text(
            modifier = Modifier.padding(0.dp),
            text = text,
            style = MaterialTheme.typography.body2.copy(
                fontFamily = yusei
            )
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(text = label)
        },
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "",
    visible: Boolean,
    onVisibilityChange: () -> Unit,
    onValueChange: (String) -> Unit = {},
) {
    val icon =
        if (visible) painterResource(id = R.drawable.visibile) else painterResource(id = R.drawable.not_visibile)
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(text = label)
        },
        shape = MaterialTheme.shapes.medium,
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable {
                    onVisibilityChange()
                },
                painter = icon,
                contentDescription = "Visibility"
            )
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun AuthButton(
    modifier: Modifier = Modifier,
    text: String = "SIGN UP",
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Orange
        )
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = text
        )
    }
}

@Composable
fun AuthenticationTitle(
    modifier: Modifier = Modifier,
    title: String,
    onBackClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onBackClicked() },
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }
        Label(
            text = title,
            style = MaterialTheme.typography.h6
        )
    }
}

fun showMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}