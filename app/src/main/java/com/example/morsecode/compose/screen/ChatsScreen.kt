package com.example.morsecode.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.model.Chat
import com.example.morsecode.compose.theme.ChatTheme
import com.example.morsecode.viewmodel.ChatsViewModel
import com.example.morsecode.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatsScreen(
    userId: String,
    chatsViewModel: ChatsViewModel,
    userViewModel: UserViewModel,
    onChatClick: (Chat) -> Unit,
    onDirectChatOpen: (String) -> Unit
) {
    val chats by chatsViewModel.chats.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()
    val users by userViewModel.users.collectAsState()
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }

    // This LaunchedEffect is for loading existing chats for the current user
    LaunchedEffect(userId) {
        // Ensure this only loads chats for the current user, not tries to create one
        chatsViewModel.loadChats(userId)
    }

    ChatTheme {
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
                            userViewModel.searchUsers(searchQuery)
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Search")
                }
            }

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

            // Display search results if a search query is active and users are found
            if (searchQuery.isNotEmpty() && users.isNotEmpty() && !isLoading) {
                LazyColumn {
                    items(users) { user ->
                        // Only show users that are not the current user in search results
                        if (user.uid != userId) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        user.uid?.let { otherUserId ->
                                            // Start a new chat with the selected user
                                            scope.launch { // Use scope to call suspend function
                                                chatsViewModel.startChat(userId, otherUserId) { chatId ->
                                                    onDirectChatOpen(chatId)
                                                }
                                            }
                                        }
                                    }
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
                }
            } else if (!isLoading) {
                // Display existing chats if no search query or no search results
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
                        }
                    }
                }
            }
        }
    }
}