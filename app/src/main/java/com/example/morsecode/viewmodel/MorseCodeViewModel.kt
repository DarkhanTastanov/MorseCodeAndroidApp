package com.example.morsecode.viewmodel

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.example.morsecode.compose.translateToMorseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sin

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
                generateMorseSound(morseText)
            }
        }
    }


    private fun getMorseMap(): Map<Char, String> {
        return if (language == "EN") CodeMaps.englishMorseCodeMap else CodeMaps.russianMorseCodeMap
    }

    private fun generateMorseSound(morseText: String) {
        val dotDuration = 150L
        val dashDuration = 5 * dotDuration
        val elementGap = dotDuration
        val characterGap = 5 * dotDuration
        val wordGap = 7 * dotDuration

        val frequency = 800.0
        val sampleRate = 44100
        val volume = 0.5f

        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val bufferSizeInBytes = if (minBufferSize > 0) minBufferSize else sampleRate // Fallback if getMinBufferSize fails

        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSizeInBytes,
            AudioTrack.MODE_STREAM
        )

        if (audioTrack.state != AudioTrack.STATE_INITIALIZED) {
            Log.e("MorseCodeApp", "AudioTrack failed to initialize")
            return
        }

        audioTrack.play()

        morseText.split(" ").forEachIndexed { wordIndex, morseWord ->
            morseWord.forEachIndexed { charIndex, char ->
                when (char) {
                    '.' -> {
                        playTone(audioTrack, frequency, dotDuration, sampleRate, volume)
                        if (charIndex < morseWord.length - 1) {
                            Thread.sleep(elementGap)
                        }
                    }
                    '-' -> {
                        playTone(audioTrack, frequency, dashDuration, sampleRate, volume)
                        if (charIndex < morseWord.length - 1) {
                            Thread.sleep(elementGap)
                        }
                    }
                }
            }

            if (wordIndex < morseText.split(" ").size - 1 && morseWord.isNotEmpty()) {
                Thread.sleep(characterGap - elementGap)
            }
            else if (wordIndex < morseText.split(" ").size - 1 && morseWord.isEmpty()) {
                Thread.sleep(wordGap)
            }
        }


        Thread.sleep(dotDuration)

        audioTrack.stop()
        audioTrack.release()
    }

    private fun playTone(
        audioTrack: AudioTrack,
        frequency: Double,
        durationMs: Long,
        sampleRate: Int,
        volume: Float
    ) {
        val numSamples = (sampleRate * durationMs / 1000).toInt()
        val buffer = ShortArray(numSamples)

        for (i in buffer.indices) {
            buffer[i] = (sin(2 * Math.PI * i / (sampleRate / frequency)) * Short.MAX_VALUE * volume).toInt().toShort()
        }

        var offset = 0
        while (offset < buffer.size) {
            val bytesWritten = audioTrack.write(buffer, offset, buffer.size - offset)
            if (bytesWritten >= 0) {
                offset += bytesWritten
            } else {
                Log.e("MorseCodeApp", "Error writing to AudioTrack: $bytesWritten")
                break
            }
        }
    }
}

object CodeMaps {
    val englishMorseCodeMap = mapOf(
        'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".", 'F' to "..-.",
        'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---", 'K' to "-.-", 'L' to ".-..",
        'M' to "--", 'N' to "-.", 'O' to "---", 'P' to ".--.", 'Q' to "--.-", 'R' to ".-.",
        'S' to "...", 'T' to "-", 'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-",
        'Y' to "-.--", 'Z' to "--..", ' ' to "/",
        '0' to "-----", '1' to ".----", '2' to "..---", '3' to "...--", '4' to "....-",
        '5' to ".....", '6' to "-....", '7' to "--...", '8' to "---..", '9' to "----."
    )

    val russianMorseCodeMap = mapOf(
        'А' to ".-", 'Б' to "-...", 'В' to ".--", 'Г' to "--.", 'Д' to "-..", 'Е' to ".",
        'Ё' to ".", 'Ж' to "...-", 'З' to "--..", 'И' to "..", 'Й' to ".---", 'К' to "-.-",
        'Л' to ".-..", 'М' to "--", 'Н' to "-.", 'О' to "---", 'П' to ".--.", 'Р' to ".-.",
        'С' to "...", 'Т' to "-", 'У' to "..-", 'Ф' to "..-.", 'Х' to "....", 'Ц' to "-.-.",
        'Ч' to "---.", 'Ш' to "----", 'Щ' to "--.-", 'Ъ' to "--.--", 'Ы' to "-.--", 'Ь' to "-..-",
        'Э' to "..-..", 'Ю' to "..--", 'Я' to ".-.-", ' ' to "/",
        '0' to "-----", '1' to ".----", '2' to "..---", '3' to "...--", '4' to "....-",
        '5' to ".....", '6' to "-....", '7' to "--...", '8' to "---..", '9' to "----."
    )
}