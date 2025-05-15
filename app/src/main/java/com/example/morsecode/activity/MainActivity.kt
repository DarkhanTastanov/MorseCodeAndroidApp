package com.example.morsecode.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.morsecode.compose.screen.AlphabetScreen
import com.example.morsecode.compose.screen.MorseCodeTranslatorScreen
import com.example.morsecode.compose.screen.LoginScreen
import com.example.morsecode.compose.theme.MorseCodeTranslatorTheme
import com.example.morsecode.viewmodel.AuthViewModel
import com.example.morsecode.viewmodel.MorseCodeViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MorseCodeTranslatorTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()
    val morseCodeViewModel: MorseCodeViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = if (authViewModel.isUserSignedIn()) "translator" else "login"
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                navController = navController
            )
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
    }
}
