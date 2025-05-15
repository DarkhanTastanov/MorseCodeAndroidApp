package com.example.morsecode.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.example.domain.languages.CodeMaps
import com.example.domain.soundplayer.MorsePlayer
import com.example.morsecode.compose.screen.translateToMorseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MorseCodeViewModel : ViewModel() {
    var inputText by mutableStateOf(TextFieldValue())
    var isMorseToText by mutableStateOf(false)
    var language by mutableStateOf("EN")

    fun playMorseSound() {
        viewModelScope.launch {
            val morseText = if (isMorseToText) {
                inputText.text
            } else {
                translateToMorseCode(inputText.text, getMorseMap())
            }

            withContext(Dispatchers.IO) {
                MorsePlayer.generateMorseSound(morseText)
            }
        }
    }


    private fun getMorseMap(): Map<Char, String> {
        return if (language == "EN") CodeMaps.englishMorseCodeMap else CodeMaps.russianMorseCodeMap
    }
}