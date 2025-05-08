package com.example.morsecode.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.morsecode.viewmodel.MorseCodeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorseCodeTranslatorScreen(
    viewModel: MorseCodeViewModel,
    onOpenAlphabet: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { onOpenAlphabet() }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = if (viewModel.isMorseToText) "Morse to Text" else "Text to Morse")
                        Spacer(modifier = Modifier.width(8.dp))
                        Box {
                            Button(onClick = { viewModel.language = if (viewModel.language == "EN") "RU" else "EN" }) {
                                Text(text = if (viewModel.language == "EN") "English" else "Russian")
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = viewModel.isMorseToText,
                            onCheckedChange = { viewModel.isMorseToText = it }
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (viewModel.isMorseToText) {
                            translateToText(viewModel.inputText.text, if (viewModel.language == "RU") russianTextMap else englishTextMap)
                        } else {
                            translateToMorseCode(viewModel.inputText.text, if (viewModel.language == "RU") russianMorseCodeMap else englishMorseCodeMap)
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    TextField(
                        value = viewModel.inputText,
                        onValueChange = { viewModel.inputText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter text") }
                    )
                }
            }
        }
    )
}


// Morse code translation functions remain unchanged
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
