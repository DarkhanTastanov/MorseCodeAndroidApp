package com.example.morsecode.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MorseCodeTranslatorScreen() {
    var inputText by remember { mutableStateOf(TextFieldValue()) }
    var isMorseToText by remember { mutableStateOf(false) }

    val translatedText = if (isMorseToText) translateToText(inputText.text) else translateToMorseCode(inputText.text)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Settings bar with switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = if (isMorseToText) "Morse to Text" else "Text to Morse")
            Switch(checked = isMorseToText, onCheckedChange = { isMorseToText = it })
        }

        // Displaying translated text
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = translatedText,
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Text input field
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter text") }
            )
        }
    }
}

// Morse code translation logic
fun translateToMorseCode(text: String): String {
    val morseCodeMap = mapOf(
        'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..",
        'E' to ".", 'F' to "..-.", 'G' to "--.", 'H' to "....",
        'I' to "..", 'J' to ".---", 'K' to "-.-", 'L' to ".-..",
        'M' to "--", 'N' to "-.", 'O' to "---", 'P' to ".--.",
        'Q' to "--.-", 'R' to ".-.", 'S' to "...", 'T' to "-",
        'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-",
        'Y' to "-.--", 'Z' to "--..",
        ' ' to "/"
    )

    return text.uppercase().map { char ->
        morseCodeMap[char] ?: ""
    }.joinToString(" ")
}

// Text to Morse translation logic
fun translateToText(morse: String): String {
    val textMap = mapOf(
        ".-" to "A", "-..." to "B", "-.-." to "C", "-.." to "D",
        "." to "E", "..-." to "F", "--." to "G", "...." to "H",
        ".." to "I", ".---" to "J", "-.-" to "K", ".-.." to "L",
        "--" to "M", "-." to "N", "---" to "O", ".--." to "P",
        "--.-" to "Q", ".-." to "R", "..." to "S", "-" to "T",
        "..-" to "U", "...-" to "V", ".--" to "W", "-..-" to "X",
        "-.--" to "Y", "--.." to "Z",
        "/" to " "
    )

    return morse.trim().split(" ").map { code ->
        textMap[code] ?: ""
    }.joinToString("")
}

@Preview(showBackground = true)
@Composable
fun PreviewMorseCodeTranslatorScreen() {
    MorseCodeTranslatorScreen()
}
