package com.sam.kladishoes.presentation.shoedetailscreen

sealed class ShoeDetailsState{
    object NotClicked:ShoeDetailsState()
    object Loading:ShoeDetailsState()
}