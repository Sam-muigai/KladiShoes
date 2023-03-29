package com.sam.kladishoes

import com.sam.kladishoes.services.AuthenticationService
import com.sam.kladishoes.services.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    authenticationService: AuthenticationService,
    storageService: StorageService
) : KladiShoesViewModel(authenticationService, storageService){




}