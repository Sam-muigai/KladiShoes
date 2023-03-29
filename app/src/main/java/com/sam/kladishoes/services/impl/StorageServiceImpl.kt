package com.sam.kladishoes.services.impl

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sam.kladishoes.model.*
import com.sam.kladishoes.presentation.shoedetailscreen.ShoeDetails
import com.sam.kladishoes.services.*
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageServiceImpl(private val db: FirebaseFirestore) : StorageService {

   private val orders = db.collection("orders")
    override val shoes: Flow<List<ShoeDetails>>
        get() = callbackFlow {
            val subscription =
                db.collection("shoes").addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        val shoes = value.map {
                            ShoeDetails(
                                id = it.getString("id").orEmpty(),
                                shoeName = it.getString("shoeName").orEmpty(),
                                category = it.getString("category").orEmpty(),
                                size = it.getString("size").orEmpty(),
                                gender = it.getString("gender").orEmpty(),
                                description = it.getString("description").orEmpty(),
                                material = it.getString("material").orEmpty(),
                                price = it.getDouble("price") ?: 0.0,
                                available = it.getBoolean("available") ?: false,
                                imageUrl = it.getString("imageUrl").orEmpty()
                            )
                        }
                        trySend(shoes)
                    }
                }
            awaitClose { subscription.remove() }
        }

    override fun getCartItems(uid: String): CartItemData {
        return try {
            val cart = callbackFlow {
                val subscription = db.collection("users")
                    .document(uid)
                    .collection("cart").addSnapshotListener { value, error ->
                        if (error != null) {
                            return@addSnapshotListener
                        }
                        if (value != null) {
                            val cartItems = value.map {
                                CartItems(
                                    uid = it.getString("uid").orEmpty(),
                                    shoeImage = it.getString("shoeImage").orEmpty(),
                                    name = it.getString("name").orEmpty(),
                                    category = it.getString("category").orEmpty(),
                                    available = false,
                                    description = it.getString("description").orEmpty(),
                                    material = it.getString("material").orEmpty(),
                                    price = it.getDouble("price") ?: 0.0,
                                    shoeId = it.getString("shoeId").orEmpty()
                                )
                            }
                            trySend(cartItems)
                        }
                    }
                awaitClose { subscription.remove() }
            }
            Data.Success(cart)
        } catch (e: Exception) {
            Data.Failure(e)
        }
    }

    override suspend fun addUser(user: User): AddUserResponse {
        return try {
            db.collection("users")
                .document(user.uid)
                .set(user).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getShoe(id: String, onSuccess: (DocumentSnapshot) -> Unit) {
        try {
            db.collection("shoes").document(id)
                .get()
                .addOnSuccessListener { document ->
                    onSuccess(document)
                }
        } catch (e: Exception) {
            Log.d("ERROR", e.message.toString())
        }
    }

    override suspend fun addShoesToCart(
        uid: String,
        shoeId: String,
        cartItems: CartItems
    ): AddToCartResponse =
        try {
            db.collection("users")
                .document(uid)
                .collection("cart")
                .document(shoeId)
                .set(cartItems)
                .await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun deleteCartItem(
        uid: String,
        shoeId: String
    ): DeleteItemResponse =
        try {
            db.collection("users")
                .document(uid)
                .collection("cart")
                .document(shoeId)
                .delete().await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun deleteAllCartItems(uid: String): DeleteAllItems = try {
        db.collection("users")
            .document(uid)
            .collection("cart")
            .get()
            .await().map {
                it.reference.delete().asDeferred()
            }.awaitAll()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }


    override suspend fun checkOut(
        uid: String,
        email: String,
        totalPrice: Double
    ): CheckOutResponse = try {
        val totalCart = db.collection("users").document(uid).collection("cart")
        val orderId = UUID.randomUUID().toString()
        val name = hashMapOf(
            "uid" to uid,
            "orderId" to orderId,
            "user_email" to email,
            "delivered" to "Pending",
            "total_price" to totalPrice
        )
        val userEmail = hashMapOf(
            "email" to email
        )
        orders.document(email).collection("orders").document(orderId).set(name).await()
        orders.document(email).set(userEmail).await()
        totalCart.get().addOnSuccessListener { querySnapShot ->
            querySnapShot.forEach { document ->
                orders.document(email)
                    .collection("orders")
                    .document(orderId)
                    .collection("order")
                    .document(document.getString("shoeId").orEmpty())
                    .set(document.data)
            }
        }.await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override fun getOrder(email: String): Flow<List<OrderList>> = callbackFlow {
        val subscription = orders.document(email).collection("orders").addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (value != null) {
                val orders = value.map {
                    OrderList(
                        delivered = it.getString("delivered").orEmpty(),
                        total_price = it.getDouble("total_price") ?: 0.0,
                        uid = it.getString("uid").orEmpty(),
                        user_name = it.getString("user_name").orEmpty()
                    )
                }
                trySend(orders)
            }
        }
        awaitClose { subscription.remove() }
    }
}