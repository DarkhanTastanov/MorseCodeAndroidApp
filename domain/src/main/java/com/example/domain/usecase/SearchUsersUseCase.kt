package com.example.domain.usecase

import com.example.domain.model.AuthUser
import com.example.domain.repository.UserRepository

class SearchUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(query: String): List<AuthUser> {
        return repository.searchUsers(query)
    }
}
