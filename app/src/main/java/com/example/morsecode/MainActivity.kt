package com.example.morsecode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
    import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.morsecode.compose.AlphabetScreen
import com.example.morsecode.compose.MorseCodeTranslatorScreen
import com.example.morsecode.compose.theme.MorseCodeTranslatorTheme
import com.example.morsecode.viewmodel.MorseCodeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val morseCodeViewModel: MorseCodeViewModel = viewModel()

            MorseCodeTranslatorTheme {
                AppNavigation(morseCodeViewModel = morseCodeViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(morseCodeViewModel: MorseCodeViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "translator"
    ) {
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