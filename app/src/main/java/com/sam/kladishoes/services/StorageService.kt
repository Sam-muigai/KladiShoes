package com.sam.kladishoes.services

import com.google.firebase.firestore.DocumentSnapshot
import com.sam.kladishoes.model.*
import kotlinx.coroutines.flow.Flow



typealias AddUserResponse = Response<Boolean>
typealias AddToCartResponse = Response<Boolean>
typealias CartItemData = Data<Flow<List<CartItems>>>
typealias DeleteItemResponse = Response<Boolean>
typealias CheckOutResponse = Response<Boolean>
typealias DeleteAllItems = Response<Boolean>

interface StorageService{

    val shoes:Flow<List<ShoeDetails>>

     fun getCartItems(uid: String):CartItemData

    suspend fun addUser(user: User):AddUserResponse

    suspend fun getShoe(id:String,onSuccess: (DocumentSnapshot) -> Unit)

    suspend fun addShoesToCart(uid:String,shoeId:String,cartItems: CartItems):AddToCartResponse

    suspend fun deleteCartItem(uid:String,shoeId: String):DeleteItemResponse

    suspend fun deleteAllCartItems(uid: String):DeleteAllItems

    suspend fun checkOut(uid:String,email:String,totalPrice:Double):CheckOutResponse

    fun getOrder(email: String):Flow<List<OrderList>>

}

