package com.sam.kladishoes.model

import java.util.*

data class ShoeDetails(
    val id:String = UUID.randomUUID().toString(),
    val shoeName:String = "",
    val category:String = "",
    val size:String = "",
    val gender:String = "",
    val description:String = "",
    val material:String = "",
    val price:Double = 0.0,
    val available:Boolean = false,
    val imageUrl:String = ""
)