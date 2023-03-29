package com.sam.kladishoes

import androidx.lifecycle.ViewModel
import com.sam.kladishoes.services.AuthenticationService
import com.sam.kladishoes.services.StorageService
import javax.inject.Inject

open class KladiShoesViewModel @Inject constructor(
    val authenticationService: AuthenticationService,
    val storageService: StorageService
):ViewModel() {

}