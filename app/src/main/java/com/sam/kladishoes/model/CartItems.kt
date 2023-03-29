package com.sam.kladishoes.model

data class CartItems(
    val uid:String,
    val shoeImage:String,
    val name:String,
    val category:String,
    val available:Boolean,
    val description:String,
    val material:String,
    val price:Double,
    val shoeId:String
)