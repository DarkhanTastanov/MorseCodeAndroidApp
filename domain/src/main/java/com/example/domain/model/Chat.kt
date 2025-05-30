package com.example.domain.model

data class Chat(
    val chatId: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L
) {
    fun getOtherParticipant(currentUserId: String): String? =
        participants.firstOrNull { it != currentUserId }
}