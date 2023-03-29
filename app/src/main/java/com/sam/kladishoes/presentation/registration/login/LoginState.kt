package com.sam.kladishoes.presentation.registration.login

sealed class LoginState{
    object NotClicked:LoginState()
    object Loading:LoginState()
}