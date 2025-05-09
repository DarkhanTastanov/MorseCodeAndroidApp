package com.example.morsecode.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.morsecode.viewmodel.CodeMaps
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
                        Spacer(modifier = Modifier.width(16.dp))

                        Box {
                            Button(onClick = { viewModel.language = if (viewModel.language == "EN") "RU" else "EN" }) {
                                Text(text = if (viewModel.language == "EN") "English" else "Russian")
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Switch(
                            checked = viewModel.isMorseToText,
                            onCheckedChange = { viewModel.isMorseToText = it }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = {
                                viewModel.playMorseSound()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Sound"
                            )
                        }
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
                            translateToMorseCode(viewModel.inputText.text, if (viewModel.language == "RU")  CodeMaps.russianMorseCodeMap else  CodeMaps.englishMorseCodeMap)
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground // Use theme color
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
                        placeholder = { Text("Enter text") },
                        colors = TextFieldDefaults.colors( // Use theme colors for TextField
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
    )
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


val englishTextMap = CodeMaps.englishMorseCodeMap.entries.associate { (k, v) -> v to k.toString() }
val russianTextMap = CodeMaps.russianMorseCodeMap.entries.associate { (k, v) -> v to k.toString() }