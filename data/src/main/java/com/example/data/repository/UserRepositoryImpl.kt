package com.example.data.repository

import com.example.domain.model.AuthUser
import com.example.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun searchUsers(query: String): List<AuthUser> {
        if (query.isBlank()) return emptyList()

        return try {
            val snapshot = firestore.collection("users")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(AuthUser::class.java)
            }.filter { user ->
                user.name?.contains(query, ignoreCase = true) == true ||
                        user.email?.contains(query, ignoreCase = true) == true
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
