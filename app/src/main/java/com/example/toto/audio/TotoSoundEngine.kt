package com.example.toto.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

object TotoSoundEngine {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var reverseBeeperJob: Job? = null
    private var alarmSirenJob: Job? = null
    private var flasherClickJob: Job? = null

    /**
     * Plays authentic dual-frequency electric vehicle horn (e.g. 430Hz + 520Hz)
     */
    fun playHorn(durationMs: Int = 300) {
        scope.launch {
            try {
                val sampleRate = 44100
                val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                val freq1 = 425.0
                val freq2 = 515.0

                for (i in 0 until numSamples) {
                    val angle1 = 2.0 * Math.PI * i / (sampleRate / freq1)
                    val angle2 = 2.0 * Math.PI * i / (sampleRate / freq2)
                    // Combine frequencies and scale
                    val sample = ((sin(angle1) * 0.5 + sin(angle2) * 0.5) * Short.MAX_VALUE * 0.85).toInt().toShort()
                    buffer[i] = sample
                }

                playBufferOnce(buffer, sampleRate)
            } catch (_: Exception) {}
        }
    }

    /**
     * Plays ignition startup chime (tri-tone ascending)
     */
    fun playPowerStartupChime() {
        scope.launch {
            val frequencies = listOf(440.0, 660.0, 880.0)
            for (f in frequencies) {
                playSingleTone(frequency = f, durationMs = 90)
                delay(40)
            }
        }
    }

    /**
     * Plays power off descending chime
     */
    fun playPowerOffChime() {
        scope.launch {
            val frequencies = listOf(880.0, 660.0, 440.0)
            for (f in frequencies) {
                playSingleTone(frequency = f, durationMs = 80)
                delay(30)
            }
        }
    }

    /**
     * Starts continuous reverse warning buzzer (Beep... Beep...)
     */
    fun startReverseBuzzer() {
        if (reverseBeeperJob?.isActive == true) return
        reverseBeeperJob = scope.launch {
            while (isActive) {
                playSingleTone(frequency = 1050.0, durationMs = 180)
                delay(220)
            }
        }
    }

    fun stopReverseBuzzer() {
        reverseBeeperJob?.cancel()
        reverseBeeperJob = null
    }

    /**
     * Starts siren alarm (Emergency / Anti-theft)
     */
    fun startAlarmSiren() {
        if (alarmSirenJob?.isActive == true) return
        alarmSirenJob = scope.launch {
            while (isActive) {
                playSingleTone(frequency = 750.0, durationMs = 250)
                playSingleTone(frequency = 1150.0, durationMs = 250)
            }
        }
    }

    fun stopAlarmSiren() {
        alarmSirenJob?.cancel()
        alarmSirenJob = null
    }

    /**
     * Plays single turn indicator relay click
     */
    fun playIndicatorClick() {
        scope.launch {
            playSingleTone(frequency = 1200.0, durationMs = 20, volume = 0.3f)
        }
    }

    private fun playSingleTone(frequency: Double, durationMs: Int, volume: Float = 0.7f) {
        try {
            val sampleRate = 44100
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val angle = 2.0 * Math.PI * i / (sampleRate / frequency)
                buffer[i] = (sin(angle) * Short.MAX_VALUE * volume).toInt().toShort()
            }
            playBufferOnce(buffer, sampleRate)
        } catch (_: Exception) {}
    }

    private fun playBufferOnce(buffer: ShortArray, sampleRate: Int) {
        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(buffer.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack.write(buffer, 0, buffer.size)
        audioTrack.play()
        // Release track after completion
        scope.launch {
            delay((buffer.size * 1000L / sampleRate) + 50)
            try {
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Exception) {}
        }
    }
}
