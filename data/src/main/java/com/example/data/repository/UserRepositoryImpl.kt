package com.example.data.repository

import com.example.domain.model.AuthUser
import com.example.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun searchUsers(query: String): List<AuthUser> {
        val users = mutableListOf<AuthUser>()
        val snapshot = firestore.collection("users")
            .whereGreaterThanOrEqualTo("name", query)
            .whereLessThanOrEqualTo("name", query + "\uf8ff")
            .get()
            .await()

        for (document in snapshot.documents) {
            val user = document.toObject(AuthUser::class.java)
            user?.let { users.add(it) }
        }

        return users
    }
}
