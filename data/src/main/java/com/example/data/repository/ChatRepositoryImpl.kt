package com.example.data.repository

import com.example.domain.model.Chat
import com.example.domain.model.Message
import com.example.domain.repository.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ChatRepositoryImpl(
    private val firestore: FirebaseFirestore
) : ChatRepository {

    override suspend fun getChatsForUser(userId: String): Flow<List<Chat>> = callbackFlow {
        val listener = firestore.collection("chats")
            .whereArrayContains("participants", userId)
            .orderBy("lastMessageTime")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val chats = snapshot.documents.mapNotNull { it.toObject(Chat::class.java) }
                trySend(chats)
            }
        awaitClose { listener.remove() }
    }


    override suspend fun getMessagesForChat(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = firestore.collection("chats/$chatId/messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val messages = snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }


    override suspend fun sendMessage(chatId: String, message: Message) {
        firestore.collection("chats/$chatId/messages")
            .add(message)
        firestore.collection("chats")
            .document(chatId)
            .update("lastMessage", message.text, "lastMessageTime", message.timestamp)
    }

    override suspend fun createOrGetChat(userAId: String, userBId: String): String {
        val snapshot = firestore.collection("chats")
            .whereArrayContains("participants", userAId)
            .get()
            .await()

        val existingChat = snapshot.documents.firstOrNull { doc ->
            val participants = doc["participants"] as? List<*>
            participants?.contains(userBId) == true
        }

        return if (existingChat != null) {
            existingChat.id
        } else {
            val newChat = firestore.collection("chats").document()
            newChat.set(
                Chat(
                    chatId = newChat.id,
                    participants = listOf(userAId, userBId),
                    lastMessage = "",
                    lastMessageTime = System.currentTimeMillis()
                )
            ).await()
            newChat.id
        }
    }
}