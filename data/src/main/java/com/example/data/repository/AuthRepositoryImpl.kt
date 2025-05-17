package com.example.data.repository

import com.example.domain.model.AuthUser
import com.example.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private var firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = firebaseAuth.signInWithCredential(credential).await()
        val user = authResult.user ?: throw IllegalStateException("User is null after Google sign-in")
        checkAndCreateUserDocument(user.uid, user.displayName, user.email)
    }

    override fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    private suspend fun checkAndCreateUserDocument(uid: String, name: String?, email: String?) {
        try {
            val docRef = firestore.collection("users").document(uid)
            val doc = docRef.get().await()
            if (!doc.exists()) {
                val userMap = hashMapOf(
                    "email" to (email ?: "Unknown"),
                    "name" to (name ?: "No Name")
                )
                docRef.set(userMap).await()
                println("User document created for UID: $uid")
            } else {
                println("User document already exists for UID: $uid")
            }
        } catch (e: Exception) {
            println("Error creating user document: ${e.message}")
            throw e
        }
    }

    override suspend fun getCurrentUser(): AuthUser {
        val user = firebaseAuth.currentUser
        return user.let {
            AuthUser(
                uid = it?.uid,
                email = it?.email,
                photoUrl = it?.photoUrl?.toString(),
                name = it?.displayName
            )
        }
    }
}
