package com.example.domain.repository

import com.example.domain.model.AuthUser

interface UserRepository {
    suspend fun searchUsers(query: String): List<AuthUser>
}
