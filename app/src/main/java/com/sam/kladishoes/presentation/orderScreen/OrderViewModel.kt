package com.sam.kladishoes.presentation.orderScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sam.kladishoes.KladiShoesViewModel
import com.sam.kladishoes.model.OrderList
import com.sam.kladishoes.services.AuthenticationService
import com.sam.kladishoes.services.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    authenticationService: AuthenticationService,
    storageService: StorageService
) : KladiShoesViewModel(authenticationService, storageService) {

    val email = authenticationService.user?.email!!
    var orders: Flow<List<OrderList>> = storageService.getOrder(email)

}