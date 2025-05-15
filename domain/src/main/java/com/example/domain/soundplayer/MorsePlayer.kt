package com.example.domain.soundplayer

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import kotlin.math.sin

object MorsePlayer {

     fun generateMorseSound(morseText: String) {
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