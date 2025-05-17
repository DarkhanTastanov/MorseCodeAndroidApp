package com.example.morsecode.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.model.AuthUser
import com.example.morsecode.compose.theme.ChatTheme
import com.example.morsecode.viewmodel.ChatsViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatsScreen(
    viewModel: ChatsViewModel,
    chats: List<Chat>,
    onChatClick: (Chat) -> Unit,
    onUserClick: (AuthUser) -> Unit
) {
    ChatTheme {
        val scope = rememberCoroutineScope()
        var searchQuery by remember { mutableStateOf("") }
        val isLoading by viewModel.isLoading.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name or email...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.searchUsers(searchQuery)
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Search")
                }
            }

            // Loading Indicator
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Display Searched Users (if any)
            val users by viewModel.users.collectAsState()
            if (searchQuery.isNotEmpty() && users.isNotEmpty() && !isLoading) {
                LazyColumn {
                    items(users) { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onUserClick(user) }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.Gray, shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                user.name?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                                user.email?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
                            }
                        }
                    }
                }
            } else if (!isLoading) {
                // Display Existing Chats
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
        }
    }
}



data class Chat(
    val lastMessage: String,
    val isDelivered: Boolean
)
