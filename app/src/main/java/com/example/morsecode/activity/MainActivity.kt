package com.example.morsecode.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.morsecode.compose.navigation.AppNavigation
import com.example.morsecode.compose.theme.MorseCodeTranslatorTheme

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