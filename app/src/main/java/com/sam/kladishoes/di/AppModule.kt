package com.sam.kladishoes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sam.kladishoes.services.AuthenticationService
import com.sam.kladishoes.services.StorageService
import com.sam.kladishoes.services.impl.AuthenticationServiceImpl
import com.sam.kladishoes.services.impl.StorageServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private fun loggingInterceptor(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthenticationService(auth:FirebaseAuth):AuthenticationService = AuthenticationServiceImpl(auth)

    @Provides
    @Singleton
    fun provideFirebaseFireStore():FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideStorageService(db:FirebaseFirestore):StorageService = StorageServiceImpl(db)

}