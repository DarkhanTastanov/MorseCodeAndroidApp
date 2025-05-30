package com.example.morsecode.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Chat
import com.example.domain.model.Message
import com.example.domain.usecase.ChatUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatsViewModel(private val chatUseCase: ChatUseCase) : ViewModel() {
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> get() = _chats

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    suspend fun getChatsForUser(userId: String): Flow<List<Chat>> =
        chatUseCase.getChatsForUser(userId)

    fun loadChats(userId: String) {
        viewModelScope.launch {
            chatUseCase.getChatsForUser(userId).collect {
                _chats.value = it
            }
        }
    }

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            chatUseCase.getMessagesForChat(chatId).collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(chatId: String, message: Message) {
        viewModelScope.launch {
            chatUseCase.sendMessage(chatId, message)
        }
    }

    suspend fun startChat(userAId: String, userBId: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val chatId = chatUseCase.createOrGetChat(userAId, userBId)
            onResult(chatId)
        }
    }
}
