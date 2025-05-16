package com.example.morsecode.compose.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.morsecode.compose.screen.AlphabetScreen
import com.example.morsecode.compose.screen.Chat
import com.example.morsecode.compose.screen.ChatScreen
import com.example.morsecode.compose.screen.ChatsScreen
import com.example.morsecode.compose.screen.LoginScreen
import com.example.morsecode.compose.screen.Message
import com.example.morsecode.compose.screen.MorseCodeTranslatorScreen
import com.example.morsecode.compose.screen.ProfileScreen
import com.example.morsecode.viewmodel.AuthViewModel
import com.example.morsecode.viewmodel.MorseCodeViewModel
import com.example.morsecode.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()
    val morseCodeViewModel: MorseCodeViewModel = viewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (authViewModel.isUserSignedIn()) "translator" else "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") { LoginScreen(authViewModel = authViewModel, navController = navController) }
            composable("translator") { MorseCodeTranslatorScreen(viewModel = morseCodeViewModel, onOpenAlphabet = { navController.navigate("alphabet") }) }
            composable("alphabet") { AlphabetScreen(viewModel = morseCodeViewModel, onBack = { navController.popBackStack() }) }
            composable("chats") {
                ChatsScreen(
                    chats = listOf(
                        Chat("Hello! How are you?", true),
                        Chat("Did you receive the file?", false)
                    ),
                    onChatClick = { chat ->
                        navController.navigate("chat/${chat.lastMessage}")
                    }
                )
            }
            composable("chat/{chatName}") { backStackEntry ->
                val chatName = backStackEntry.arguments?.getString("chatName") ?: "Unknown Chat"
                ChatScreen(
                    chatName = chatName,
                    messages = listOf(
                        Message("Hello! How are you?", false),
                        Message("I'm good, thanks! You?", true)
                    ),
                    onSendMessage = { messageText ->
                        // Handle message sending logic
                    }
                )
            }
            composable("profile") {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onEditProfile = { /* Navigate to Edit Profile Screen */ },
                    onAppSettings = { /* Navigate to App Settings Screen */ }
                )            }
        }
    }
}

