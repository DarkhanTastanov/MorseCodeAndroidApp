package com.example.domain.usecase

import com.example.domain.model.AuthUser
import com.example.domain.repository.AuthRepository

class AuthUseCase(private val repository: AuthRepository) {

    suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return repository.signInWithGoogle(idToken)
    }

    fun isUserSignedIn(): Boolean {
        return repository.isUserSignedIn()
    }


    suspend fun returnAuth(): AuthUser {
        return repository.returnAuth()
    }
}
