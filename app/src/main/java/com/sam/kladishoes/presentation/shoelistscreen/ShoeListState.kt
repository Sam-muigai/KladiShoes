package com.sam.kladishoes.presentation.shoelistscreen

sealed class ShoeListState{
    object NotLoading:ShoeListState()
    object Loading:ShoeListState()
}