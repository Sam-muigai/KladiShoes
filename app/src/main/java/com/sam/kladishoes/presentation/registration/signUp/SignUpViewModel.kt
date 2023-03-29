package com.sam.kladishoes.presentation.registration.signUp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sam.kladishoes.KladiShoesViewModel
import com.sam.kladishoes.model.Response
import com.sam.kladishoes.model.User
import com.sam.kladishoes.services.AuthenticationService
import com.sam.kladishoes.services.SignUpResponse
import com.sam.kladishoes.services.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    authenticationService: AuthenticationService,
    storageService: StorageService,
    savedStateHandle: SavedStateHandle
) : KladiShoesViewModel(authenticationService, storageService) {
    var signUpResponse = mutableStateOf<SignUpResponse>(Response.Success(false))
        private set

    val userName = mutableStateOf("")
    val userEmail = mutableStateOf("")
    val phone = mutableStateOf("")
    val location = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    var shoeId by mutableStateOf("")
    val user:MutableState<User> = mutableStateOf(User())

    fun onUserNameChange(input:String){
        userName.value = input
    }
    fun onUserEmailChange(input:String){
        userEmail.value = input
    }
    fun onUserPhoneChange(input:String){
        phone.value = input
    }
    fun onLocationChange(input:String){
        location.value = input
    }
    fun onUserPasswordChange(input:String){
        password.value = input
    }
    fun onConfirmPasswordChange(input:String){
        confirmPassword.value = input
    }


    fun signUp():String? {
        var message = ""
        if (userName.value.isEmpty()) {
            message = "Username cannot be blank"
            return message
        }
        if (userEmail.value.isEmpty()) {
            message = "Email cannot be blank"
            return message
        }
        if (phone.value.isEmpty()) {
            message = "Phone cannot be blank"
            return message
        }
        if (location.value.isEmpty()) {
            message = "Location cannot be blank"
            return message
        }
        if (password.value.isEmpty()) {
            message = "Password cannot be blank"
            return message
        }
        if (password.value != confirmPassword.value) {
            message = "Password and confirm password do not match!"
            return message
        }
        signUpUser(userEmail.value,password.value)
        return null
    }


    private fun signUpUser(email:String,password:String){
        viewModelScope.launch {
            signUpResponse.value = Response.Loading
            signUpResponse.value = authenticationService.signUpWithEmailAndPassword(email, password)
            val user = User(
                uid = authenticationService.user?.uid!!,
                name= userName.value,
                email = userEmail.value,
                phone = phone.value.toInt(),
                location = location.value)
            addUser(user)
        }
    }


    private fun addUser(user: User){
        viewModelScope.launch {
            storageService.addUser(user)
        }
    }

}