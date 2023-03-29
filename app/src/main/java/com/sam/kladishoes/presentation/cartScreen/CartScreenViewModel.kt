package com.sam.kladishoes.presentation.cartScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.sam.kladishoes.KladiShoesViewModel
import com.sam.kladishoes.model.CartItems
import com.sam.kladishoes.model.Data
import com.sam.kladishoes.services.AuthenticationService
import com.sam.kladishoes.services.CartItemData
import com.sam.kladishoes.services.StorageService
import com.sam.kladishoes.utils.darajaDriver
import com.sam.kladishoes.utils.stkPushRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CartScreenViewModel @Inject constructor(
    storageService: StorageService,
    authenticationService: AuthenticationService,
) : KladiShoesViewModel(authenticationService, storageService) {


    private val uid = authenticationService.user?.uid!!
    private val email = authenticationService.user?.email!!
    var userName by mutableStateOf("")
    private val initialCart: Flow<List<CartItems>> = flow {}
    val userCart = mutableStateOf<CartItemData>(Data.Success(initialCart))
    val state = MutableStateFlow<CartScreenState>(CartScreenState.NotClicked)


    init {
        getCartItems(uid)
    }


    private fun getCartItems(uid: String) {
        viewModelScope.launch {
            userCart.value = Data.Loading
            userCart.value = storageService.getCartItems(uid)
        }
    }


    fun checkOut(phoneNumber: String, totalPrice: Double, onCompletion: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            state.value = CartScreenState.Loading
            withContext(Dispatchers.IO){
                storageService.checkOut(
                    uid,
                    email,
                    totalPrice = totalPrice
                )
                deleteAllCartItems()
            }
            delay(1000)
            val confirm = async { onConfirm(phoneNumber = phoneNumber, totalAmount = "${totalPrice.toInt()}") }
            confirm.await()
            delay(3000)
            state.value = CartScreenState.NotClicked
            onCompletion()
        }
    }

    private fun onConfirm(
        phoneNumber: String,
        totalAmount: String
    ) {
        viewModelScope.launch {
            if (phoneNumber.isNotEmpty()) {
                darajaDriver.performStkPush(
                    stkPushRequest = stkPushRequest(phoneNumber, totalAmount)
                )
            }
        }
    }

    fun deleteAllCartItems() {
        viewModelScope.launch {
            storageService.deleteAllCartItems(uid)
        }
    }

    fun deleteShoe(shoeId: String) {
        viewModelScope.launch {
            storageService.deleteCartItem(
                uid = uid,
                shoeId = shoeId
            )
        }
    }
}