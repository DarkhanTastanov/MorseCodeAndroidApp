package com.example.morsecode.di

import com.example.data.remote.GoogleSignInIntentProvider
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.GoogleSignInIntentProviderImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.usecase.AuthUseCase
import com.example.morsecode.viewmodel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Firebase.auth }
    single { FirebaseFirestore.getInstance() }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    single { AuthUseCase(get()) }

    single<GoogleSignInIntentProvider> { GoogleSignInIntentProviderImpl(get()) }

    viewModel { AuthViewModel(get(), get<GoogleSignInIntentProvider>()) }
}
