package com.example.morsecode.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChatsScreen(chats: List<Chat>, onChatClick: (Chat) -> Unit) {
    LazyColumn {
        items(chats) { chat ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .clickable { onChatClick(chat) }
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Gray, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = chat.lastMessage,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    imageVector = if (chat.isDelivered) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = if (chat.isDelivered) "Delivered" else "Failed",
                    tint = if (chat.isDelivered) Color.Green else Color.Red
                )
            }
        }
    }
}

data class Chat(
    val lastMessage: String,
    val isDelivered: Boolean
)

@Preview
@Composable
fun PreviewChatsScreen() {
    val sampleChats = listOf(
        Chat("Hello! How are you?", true),
        Chat("Did you receive the file?", false)
    )
    ChatsScreen(chats = sampleChats, onChatClick = {})
}
