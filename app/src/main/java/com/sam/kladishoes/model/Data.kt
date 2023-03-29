package com.sam.kladishoes.model

sealed class Data<out T>{
    object Loading:Data<Nothing>()
    data class Success<T>(val data:T):Data<T>()
    data class Failure(val e:Throwable):Data<Nothing>()
}
