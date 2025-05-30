package com.example.morsecode.compose.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.morsecode.compose.screen.AlphabetScreen
import com.example.morsecode.compose.screen.ChatScreen
import com.example.morsecode.compose.screen.ChatsScreen
import com.example.morsecode.compose.screen.LoginScreen
import com.example.morsecode.compose.screen.MorseCodeTranslatorScreen
import com.example.morsecode.compose.screen.ProfileScreen
import com.example.morsecode.viewmodel.AuthViewModel
import com.example.morsecode.viewmodel.ChatsViewModel
import com.example.morsecode.viewmodel.UserViewModel
import com.example.morsecode.viewmodel.MorseCodeViewModel
import com.example.morsecode.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()
    val morseCodeViewModel: MorseCodeViewModel = viewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()
    val userViewModel: UserViewModel = koinViewModel()
    val chatsViewModel: ChatsViewModel = koinViewModel()
    var userId: String? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        userId = authViewModel.getCurrentUserId()
    }

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
            composable("login") {
                LoginScreen(authViewModel = authViewModel, navController = navController)
            }
            composable("translator") {
                MorseCodeTranslatorScreen(
                    viewModel = morseCodeViewModel,
                    onOpenAlphabet = { navController.navigate("alphabet") }
                )
            }
            composable("alphabet") {
                AlphabetScreen(
                    viewModel = morseCodeViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("chats") {
                userId?.let { it1 ->
                    ChatsScreen(
                        chatsViewModel = chatsViewModel,
                        userViewModel = userViewModel,
                        userId = it1,
                        onChatClick = { chat ->
                            navController.navigate("chat/${chat.chatId}")
                        },
                        onDirectChatOpen = { chatId ->
                            navController.navigate("chat/$chatId")
                        }
                    )
                }
            }


            composable("chat/{chatId}") { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable
                userId?.let {
                    ChatScreen(
                        chatId = chatId,
                        chatsViewModel = chatsViewModel,
                        userId = it
                    )
                }
            }



            composable("profile") {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onEditProfile = { /* Navigate to Edit Profile Screen */ },
                    onAppSettings = { /* Navigate to App Settings Screen */ }
                )
            }
        }
    }
}
