package com.example.domain.repository

import com.example.domain.model.Chat
import com.example.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChatsForUser(userId: String): Flow<List<Chat>>
    suspend fun getMessagesForChat(chatId: String): Flow<List<Message>>
    suspend fun sendMessage(chatId: String, message: Message)
    suspend fun createOrGetChat(userAId: String, userBId: String): String
}