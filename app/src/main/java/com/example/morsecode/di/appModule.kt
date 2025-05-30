package com.example.morsecode.di

import com.example.data.remote.GoogleSignInIntentProvider
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.ChatRepositoryImpl
import com.example.data.repository.GoogleSignInIntentProviderImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.ChatRepository
import com.example.domain.repository.UserRepository
import com.example.domain.usecase.AuthUseCase
import com.example.domain.usecase.ChatUseCase
import com.example.domain.usecase.SearchUsersUseCase
import com.example.morsecode.viewmodel.AuthViewModel
import com.example.morsecode.viewmodel.ChatsViewModel
import com.example.morsecode.viewmodel.UserViewModel
import com.example.morsecode.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Firebase.auth }
    single { FirebaseFirestore.getInstance() }

//    auth
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single { AuthUseCase(get()) }
    single<GoogleSignInIntentProvider> { GoogleSignInIntentProviderImpl(get()) }
    viewModel { AuthViewModel(get(), get<GoogleSignInIntentProvider>()) }

//profile show
    viewModel { ProfileViewModel(get()) }

//user search
    single<UserRepository> { UserRepositoryImpl(get()) }
    factory { SearchUsersUseCase(get()) }
    viewModel { UserViewModel(get()) }
//chats
    single<ChatRepository> { ChatRepositoryImpl(get()) }
    single { ChatUseCase(get()) }
    viewModel { ChatsViewModel(get()) }
}
