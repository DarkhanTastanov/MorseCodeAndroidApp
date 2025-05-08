package com.example.morsecode.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

class MorseCodeViewModel : ViewModel() {
    var inputText by mutableStateOf(TextFieldValue())
    var isMorseToText by mutableStateOf(false)
    var language by mutableStateOf("EN")
}
