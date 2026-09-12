package com.example.toonstudio.engine

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.toonstudio.model.CharacterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class VoiceSpeechEngine(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentSpeaker = MutableStateFlow<CharacterType?>(null)
    val currentSpeaker: StateFlow<CharacterType?> = _currentSpeaker.asStateFlow()

    private var onSpeechDoneCallback: (() -> Unit)? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            setupProgressListener()
            // Try Bengali first
            val bnLocale = Locale("bn", "BD")
            val langResult = tts?.setLanguage(bnLocale)
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                val inLocale = Locale("bn", "IN")
                if (tts?.setLanguage(inLocale) == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.language = Locale.getDefault()
                }
            }
        } else {
            Log.e("VoiceSpeechEngine", "TTS initialization failed with status $status")
        }
    }

    private fun setupProgressListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isSpeaking.value = true
            }

            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = false
                _currentSpeaker.value = null
                onSpeechDoneCallback?.invoke()
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
                _currentSpeaker.value = null
                onSpeechDoneCallback?.invoke()
            }
        })
    }

    fun speak(
        text: String,
        character: CharacterType?,
        onDone: (() -> Unit)? = null
    ) {
        if (!isInitialized || text.isBlank()) {
            onDone?.invoke()
            return
        }

        onSpeechDoneCallback = onDone
        _currentSpeaker.value = character

        // Set Pitch and Speed according to character personality
        val pitch = character?.voicePitch ?: 1.0f
        val speed = character?.voiceSpeed ?: 1.0f

        tts?.setPitch(pitch)
        tts?.setSpeechRate(speed)

        // Check if text has Bengali characters
        val hasBengali = text.any { it in '\u0980'..'\u09FF' }
        if (hasBengali) {
            val bn = Locale("bn", "BD")
            val available = tts?.isLanguageAvailable(bn) ?: TextToSpeech.LANG_NOT_SUPPORTED
            if (available >= TextToSpeech.LANG_AVAILABLE) {
                tts?.language = bn
            } else {
                tts?.language = Locale("bn", "IN")
            }
        }

        val utteranceId = "utterance_${System.currentTimeMillis()}"
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _currentSpeaker.value = null
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("VoiceSpeechEngine", "Error shutting down TTS", e)
        }
    }
}
