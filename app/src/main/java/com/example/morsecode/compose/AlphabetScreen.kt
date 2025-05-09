package com.example.morsecode.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.morsecode.viewmodel.CodeMaps
import com.example.morsecode.viewmodel.MorseCodeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlphabetScreen(
    viewModel: MorseCodeViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Morse Code Alphabet") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val alphabetMap = if (viewModel.language == "RU")  CodeMaps.russianMorseCodeMap else  CodeMaps.englishMorseCodeMap

                item {
                    Text(
                        text = if (viewModel.language == "RU") "Russian Alphabet" else "English Alphabet",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(alphabetMap.entries.toList()) { (letter, morse) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = letter.toString(), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground) // Use theme color
                        Text(text = morse, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground) // Use theme color
                    }
                }
            }
        }
    )
}