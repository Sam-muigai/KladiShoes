package com.sam.kladishoes.presentation.cartScreen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.sam.kladishoes.model.CartItems
import com.sam.kladishoes.model.Data
import com.sam.kladishoes.presentation.components.*
import com.sam.kladishoes.utils.darajaDriver
import com.sam.kladishoes.utils.stkPushRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CartScreen(
    viewModel: CartScreenViewModel = hiltViewModel(),
    onCheckOutClicked: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    var shoePrice by remember {
        mutableStateOf(0.0)
    }
    val deliveryPrice by remember {
        mutableStateOf(0.0)
    }
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    when (val response = viewModel.userCart.value) {
        is Data.Success -> {
            val cartItems = response.data.collectAsState(initial = emptyList())
            shoePrice = cartItems.value.sumOf { it.price }
            CartScreen(
                onBackPressed,
                scaffoldState,
                scrollState,
                shoePrice,
                deliveryPrice,
                cartItems.value.isNotEmpty(),
                viewModel::deleteAllCartItems,
                cartItems.value,
                viewModel::deleteShoe,
            ) {
                onCheckOutClicked(shoePrice.toString())
                //viewModel.checkOut(shoePrice)
            }
        }
        is Data.Loading -> {
            LoadingScreen()
        }
        is Data.Failure -> Unit
    }
}

@Composable
private fun CartScreen(
    onBackPressed: () -> Unit,
    scaffoldState: ScaffoldState,
    scrollState: ScrollState,
    shoePrice: Double,
    deliveryPrice: Double,
    buttonEnabled: Boolean,
    onDeleteAllClicked: () -> Unit,
    cartItems: List<CartItems>,
    deleteShoe: (String) -> Unit,
    onCheckOutClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TitleSection(
                leadingIcon = Icons.Default.ArrowBack,
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable { onDeleteAllClicked() },
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null
                    )
                },
                text = "Your Cart",
                onLeadingIconClicked = {
                    onBackPressed()
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomButton(
                    shape = MaterialTheme.shapes.small,
                    text = "CHECK OUT",
                    enabled = buttonEnabled
                ) {
                    onCheckOutClicked()
                }
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(scrollState)
        ) {
            TotalCarts(cartItems) { shoeId ->
                deleteShoe(shoeId)
            }
            Spacer(modifier = Modifier.height(50.dp))
            Calculate(price = shoePrice, heading = "Goods")
            Calculate(price = deliveryPrice, heading = "Delivery")
            Divider()
            Spacer(modifier = Modifier.height(30.dp))
            Calculate(price = shoePrice, heading = "Total Price:")
        }
    }
}


@Composable
fun TotalCarts(cartItems: List<CartItems>, onDeleteClicked: (String) -> Unit) {
    cartItems.forEach {
        CartItem(
            imageUrl = it.shoeImage,
            price = it.price,
            shoeName = it.name
        ) {
            onDeleteClicked(it.shoeId)
        }
    }
}

@Composable
fun Calculate(
    modifier: Modifier = Modifier,
    price: Double = 500.0,
    heading: String = "Goods"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Label(
            text = heading,
            style = MaterialTheme.typography.body1
        )
        Label(
            text = "$$price",
            style = MaterialTheme.typography.body1
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartItem(
    modifier: Modifier = Modifier,
    imageUrl: String = "https://images.pexels.com/photos/19090/pexels-photo.jpg?auto=compress&cs=tinysrgb&w=600",
    price: Double = 0.0,
    shoeName: String = "Air Jordan",
    onDeleteClicked: () -> Unit = {}
) {

    Column(modifier = modifier) {
        ListItem(
            icon = {
                LabelImage(
                    shape = RoundedCornerShape(8.dp),
                    data = imageUrl
                )
            },
            secondaryText = {
                Label(
                    text = "KSH $price",
                    style = MaterialTheme.typography.body1
                )
            },
            trailing = {
                Icon(
                    modifier = Modifier.clickable { onDeleteClicked() },
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        ) {
            Label(
                text = shoeName,
                style = MaterialTheme.typography.body1
            )
        }
        Divider()
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PurchaseDialog(
    modifier: Modifier = Modifier,
    totalAmount: String = "2400",
    state: CartScreenState,
    onCheckOutClicked: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var phoneNumber by rememberSaveable {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val focus = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Surface(
            modifier = modifier
                .wrapContentSize(),
            color = MaterialTheme.colors.background,
            shape = MaterialTheme.shapes.large
        ) {
            if (state == CartScreenState.NotClicked) {
                Column(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Pay Through Mpesa")
                    Text(text = "Total Amount:Ksh $totalAmount")
                    Text(text = "Enter your (Mpesa) phone number")
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            leadingIcon = {
                                Text(text = " +254 ")
                            },
                            modifier = Modifier,
                            value = phoneNumber,
                            onValueChange = {
                                phoneNumber = it
                            },
                            shape = MaterialTheme.shapes.large,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                }
                            ),
                            label = {
                                Text(text = "Mpesa Number")
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                onDismiss()
                            }
                        ) {
                            Text(text = "Cancel")
                        }
                        Button(
                            onClick = {
                                focus.clearFocus()
                                onCheckOutClicked(phoneNumber, totalAmount)
                                showMessage(context, "Order will be processed soon")
                            },
                            enabled = phoneNumber.length == 9
                        ) {
                            Text(text = "Confirm")
                        }
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

