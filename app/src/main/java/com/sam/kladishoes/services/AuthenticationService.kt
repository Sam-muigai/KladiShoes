package com.sam.kladishoes.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sam.kladishoes.model.Response
import kotlinx.coroutines.flow.Flow

typealias SignUpResponse = Response<Boolean>
typealias LoginResponse = Response<Boolean>
typealias SignOutResponse = Response<Boolean>

interface AuthenticationService{
    val user:FirebaseUser?

    val currentUse:Flow<FirebaseUser?>

    suspend fun signUpWithEmailAndPassword(email:String, password:String):SignUpResponse

    suspend fun logInWithEmailAndPassword(email:String,password:String):LoginResponse

    suspend fun signOut():SignOutResponse

}