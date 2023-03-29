package com.sam.kladishoes.presentation.shoelistscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.sam.kladishoes.KladiShoesViewModel
import com.sam.kladishoes.model.Response
import com.sam.kladishoes.model.ShoeDetails
import com.sam.kladishoes.services.AuthenticationService
import com.sam.kladishoes.services.SignOutResponse
import com.sam.kladishoes.services.StorageService
import com.sam.kladishoes.utils.ROUTES
import com.sam.kladishoes.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoeListViewModel @Inject constructor(
    authenticationService: AuthenticationService,
    storageService: StorageService
): KladiShoesViewModel(authenticationService,storageService) {


    var shoes:Flow<List<ShoeDetails>> = storageService.shoes


    var signOutResponse = mutableStateOf<SignOutResponse>(Response.Success(false))
        private set


    val email = authenticationService.user?.email!!
    fun signOut(){
        viewModelScope.launch {
            signOutResponse.value = Response.Loading
            delay(2000)
            signOutResponse.value = authenticationService.signOut()
        }
    }



}