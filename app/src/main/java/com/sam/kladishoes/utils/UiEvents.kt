package com.sam.kladishoes.utils

sealed class UiEvents{
    data class Navigate(val route:String):UiEvents()
    data class ShowSnackBar(val message:String,val action:String? = null):UiEvents()
    object PopBackStack:UiEvents()
}
