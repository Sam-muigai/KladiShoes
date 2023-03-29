package com.sam.kladishoes.presentation.shoedetailscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sam.kladishoes.model.ShoeDetails
import com.sam.kladishoes.KladiShoesViewModel
import com.sam.kladishoes.model.CartItems
import com.sam.kladishoes.model.Response
import com.sam.kladishoes.services.AddToCartResponse
import com.sam.kladishoes.services.AuthenticationService
import com.sam.kladishoes.services.StorageService
import com.sam.kladishoes.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoeDetailsViewModel @Inject constructor(
    authenticationService: AuthenticationService,
    storageService: StorageService,
    savedStateHandle: SavedStateHandle
) : KladiShoesViewModel(authenticationService, storageService) {

    private val uid = authenticationService.user?.uid!!

    val state: MutableState<ShoeDetailsState> = mutableStateOf(ShoeDetailsState.NotClicked)

    var addToCartResponse = mutableStateOf<AddToCartResponse>(Response.Success(false))
       private set

    val clickedShoe: MutableState<ShoeDetails> = mutableStateOf(ShoeDetails())

    init {
        val id = savedStateHandle.get<String>("shoeId")!!
        viewModelScope.launch {
            storageService.getShoe(id) { document ->
                val shoe = ShoeDetails(
                    id = document.getString("id").orEmpty(),
                    shoeName = document.getString("shoeName").orEmpty(),
                    category = document.getString("category").orEmpty(),
                    size = document.getString("size").orEmpty(),
                    gender = document.getString("gender").orEmpty(),
                    description = document.getString("description").orEmpty(),
                    material = document.getString("material").orEmpty(),
                    price = document.getDouble("price") ?: 0.0,
                    imageUrl = document.getString("imageUrl").orEmpty(),
                    available = document.getBoolean("available") ?: false
                )
                clickedShoe.value = shoe
            }
        }
    }
     fun addToCart() {
        viewModelScope.launch {
            addToCartResponse.value = Response.Loading
                val cartItems = CartItems(
                    uid = uid,
                    shoeImage = clickedShoe.value.imageUrl,
                    name = clickedShoe.value.shoeName,
                    category = clickedShoe.value.category,
                    available = clickedShoe.value.available,
                    description = clickedShoe.value.description,
                    material = clickedShoe.value.material,
                    price = clickedShoe.value.price,
                    shoeId = clickedShoe.value.id
                )
            delay(1000)
            addToCartResponse.value = storageService.addShoesToCart(
                    uid = uid,
                    shoeId = clickedShoe.value.id,
                    cartItems = cartItems
                )
            delay(50)
            addToCartResponse.value = Response.Success(false)
        }
    }
}