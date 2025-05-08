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
    var language by remember { mutableStateOf("EN") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val translatedText = when (language) {
        "RU" -> if (isMorseToText) translateToText(inputText.text, russianTextMap) else translateToMorseCode(inputText.text, russianMorseCodeMap)
        else -> if (isMorseToText) translateToText(inputText.text, englishTextMap) else translateToMorseCode(inputText.text, englishMorseCodeMap)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Settings bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = if (isMorseToText) "Morse to Text" else "Text to Morse")

            Box {
                Button(onClick = { isDropdownExpanded = true }) {
                    Text(text = when (language) {
                        "EN" -> "English"
                        "RU" -> "Russian"
                        else -> "Select Language"
                    })
                }

                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("English") },
                        onClick = {
                            language = "EN"
                            isDropdownExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Russian") },
                        onClick = {
                            language = "RU"
                            isDropdownExpanded = false
                        }
                    )
                }
            }

            Switch(checked = isMorseToText, onCheckedChange = { isMorseToText = it })
        }

        // Translated text
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

        // Input field
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


fun translateToMorseCode(text: String, morseCodeMap: Map<Char, String>): String {
    return text.uppercase().map { char ->
        morseCodeMap[char] ?: ""
    }.joinToString(" ")
}

fun translateToText(morse: String, textMap: Map<String, String>): String {
    return morse.trim().split(" ").map { code ->
        textMap[code] ?: ""
    }.joinToString("")
}

val englishMorseCodeMap = mapOf(
    'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".", 'F' to "..-.",
    'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---", 'K' to "-.-", 'L' to ".-..",
    'M' to "--", 'N' to "-.", 'O' to "---", 'P' to ".--.", 'Q' to "--.-", 'R' to ".-.",
    'S' to "...", 'T' to "-", 'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-",
    'Y' to "-.--", 'Z' to "--..", ' ' to "/"
)

val russianMorseCodeMap = mapOf(
    'А' to ".-", 'Б' to "-...", 'В' to ".--", 'Г' to "--.", 'Д' to "-..", 'Е' to ".",
    'Ё' to ".", 'Ж' to "...-", 'З' to "--..", 'И' to "..", 'Й' to ".---", 'К' to "-.-",
    'Л' to ".-..", 'М' to "--", 'Н' to "-.", 'О' to "---", 'П' to ".--.", 'Р' to ".-.",
    'С' to "...", 'Т' to "-", 'У' to "..-", 'Ф' to "..-.", 'Х' to "....", 'Ц' to "-.-.",
    'Ч' to "---.", 'Ш' to "----", 'Щ' to "--.-", 'Ъ' to "--.--", 'Ы' to "-.--", 'Ь' to "-..-",
    'Э' to "..-..", 'Ю' to "..--", 'Я' to ".-.-", ' ' to "/"
)

val englishTextMap = englishMorseCodeMap.entries.associate { (k, v) -> v to k.toString() }
val russianTextMap = russianMorseCodeMap.entries.associate { (k, v) -> v to k.toString() }

@Preview(showBackground = true)
@Composable
fun PreviewMorseCodeTranslatorScreen() {
    MorseCodeTranslatorScreen()
}
