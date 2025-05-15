package com.example.domain.repository

import com.example.domain.model.AuthUser

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    fun isUserSignedIn(): Boolean
    suspend fun returnAuth(): AuthUser
}
