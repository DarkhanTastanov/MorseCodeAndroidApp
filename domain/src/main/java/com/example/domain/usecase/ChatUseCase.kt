package com.example.domain.usecase

import com.example.domain.model.Message
import com.example.domain.repository.ChatRepository

class ChatUseCase(private val repository: ChatRepository) {
    suspend fun getChatsForUser(userId: String) = repository.getChatsForUser(userId)
    suspend fun getMessagesForChat(chatId: String) = repository.getMessagesForChat(chatId)
    suspend fun sendMessage(chatId: String, message: Message) = repository.sendMessage(chatId, message)
    suspend fun createOrGetChat(userAId: String, userBId: String) = repository.createOrGetChat(userAId, userBId)
}