package com.sam.kladishoes.presentation.registration.login

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sam.kladishoes.KladiShoesViewModel
import com.sam.kladishoes.model.Response
import com.sam.kladishoes.services.AuthenticationService
import com.sam.kladishoes.services.LoginResponse
import com.sam.kladishoes.services.StorageService
import com.sam.kladishoes.utils.ROUTES
import com.sam.kladishoes.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    authenticationService: AuthenticationService,
    storageService: StorageService
) : KladiShoesViewModel(authenticationService, storageService) {

    var shoeId by mutableStateOf("")

    val loginResponse = mutableStateOf<LoginResponse>(Response.Success(false))

    val email = mutableStateOf("")
    val password = mutableStateOf("")

    fun onPasswordChange(input:String){
        password.value = input
    }
    fun onEmailChange(input:String){
        email.value = input
    }

    fun login(): String? {
        var message = ""
        if (email.value.isEmpty()) {
            message = "Email cannot be empty"
            return message
        }
        if (password.value.isEmpty()) {
            message = "Password field is Empty"
            return message
        }
        viewModelScope.launch {
            loginResponse.value = Response.Loading
            loginResponse.value = authenticationService.logInWithEmailAndPassword(email.value, password.value)
        }
        return null
    }
}
