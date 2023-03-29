package com.sam.kladishoes.presentation.cartScreen

sealed class CartScreenState{
    object NotClicked:CartScreenState()
    object Loading:CartScreenState()
}