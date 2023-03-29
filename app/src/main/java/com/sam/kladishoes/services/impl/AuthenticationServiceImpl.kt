package com.sam.kladishoes.services.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.sam.kladishoes.model.Response
import com.sam.kladishoes.services.AuthenticationService
import com.sam.kladishoes.services.LoginResponse
import com.sam.kladishoes.services.SignOutResponse
import com.sam.kladishoes.services.SignUpResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationServiceImpl @Inject constructor(private val auth: FirebaseAuth) :
    AuthenticationService {
    override val user: FirebaseUser? get() = auth.currentUser

    override val currentUse: Flow<FirebaseUser?>
        get() = callbackFlow {
            val subscription = FirebaseAuth.AuthStateListener { firebaseAuth ->
                trySend(firebaseAuth.currentUser)
            }
            auth.addAuthStateListener(subscription)
            awaitClose { auth.removeAuthStateListener(subscription) }
        }

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): SignUpResponse {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun logInWithEmailAndPassword(
        email: String,
        password: String
    ): LoginResponse {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun signOut(): SignOutResponse =
        try {
            auth.signOut()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }


}