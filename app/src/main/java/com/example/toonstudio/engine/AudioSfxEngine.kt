package com.example.toonstudio.engine

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import com.example.toonstudio.model.ActionSoundEffect
import com.example.toonstudio.model.BgmStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AudioSfxEngine {

    private var toneGen: ToneGenerator? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private var bgmJob: Job? = null

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 75)
        } catch (e: Exception) {
            Log.e("AudioSfxEngine", "Failed to initialize ToneGenerator", e)
        }
    }

    fun playSfx(sfx: ActionSoundEffect) {
        scope.launch {
            try {
                when (sfx) {
                    ActionSoundEffect.PUNCH_HIT -> {
                        toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 180)
                    }
                    ActionSoundEffect.MAGIC_SPELL -> {
                        toneGen?.startTone(ToneGenerator.TONE_DTMF_D, 120)
                        delay(100)
                        toneGen?.startTone(ToneGenerator.TONE_DTMF_C, 120)
                        delay(100)
                        toneGen?.startTone(ToneGenerator.TONE_DTMF_A, 200)
                    }
                    ActionSoundEffect.THUNDER_CRACK -> {
                        toneGen?.startTone(ToneGenerator.TONE_PROP_PROMPT, 300)
                        delay(150)
                        toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 200)
                    }
                    ActionSoundEffect.APPLAUSE -> {
                        repeat(5) {
                            toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 80)
                            delay(100)
                        }
                    }
                    ActionSoundEffect.LAUGHTER -> {
                        repeat(4) {
                            toneGen?.startTone(ToneGenerator.TONE_DTMF_9, 90)
                            delay(120)
                        }
                    }
                    ActionSoundEffect.GASP -> {
                        toneGen?.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 220)
                    }
                    ActionSoundEffect.FOOTSTEPS -> {
                        repeat(3) {
                            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 70)
                            delay(200)
                        }
                    }
                    ActionSoundEffect.NONE -> {}
                }
            } catch (e: Exception) {
                Log.e("AudioSfxEngine", "Error playing sfx", e)
            }
        }
    }

    fun playBgm(style: BgmStyle) {
        bgmJob?.cancel()
        if (style == BgmStyle.NONE) return

        bgmJob = scope.launch {
            try {
                val notes = when (style) {
                    BgmStyle.CHEERFUL_VILLAGE -> listOf(
                        ToneGenerator.TONE_DTMF_1 to 180L,
                        ToneGenerator.TONE_DTMF_3 to 180L,
                        ToneGenerator.TONE_DTMF_5 to 220L,
                        ToneGenerator.TONE_DTMF_8 to 300L
                    )
                    BgmStyle.ACTION_CHASE -> listOf(
                        ToneGenerator.TONE_DTMF_7 to 100L,
                        ToneGenerator.TONE_DTMF_9 to 100L,
                        ToneGenerator.TONE_DTMF_4 to 120L,
                        ToneGenerator.TONE_DTMF_6 to 140L
                    )
                    BgmStyle.SUSPENSE_DRAMA -> listOf(
                        ToneGenerator.TONE_DTMF_2 to 250L,
                        ToneGenerator.TONE_DTMF_0 to 350L,
                        ToneGenerator.TONE_DTMF_8 to 300L
                    )
                    BgmStyle.COMEDY_FUNNY -> listOf(
                        ToneGenerator.TONE_DTMF_4 to 120L,
                        ToneGenerator.TONE_DTMF_5 to 120L,
                        ToneGenerator.TONE_DTMF_6 to 160L,
                        ToneGenerator.TONE_DTMF_1 to 240L
                    )
                    BgmStyle.SPOOKY_MYSTERY -> listOf(
                        ToneGenerator.TONE_DTMF_0 to 300L,
                        ToneGenerator.TONE_DTMF_8 to 300L
                    )
                    BgmStyle.EMOTIONAL -> listOf(
                        ToneGenerator.TONE_DTMF_3 to 300L,
                        ToneGenerator.TONE_DTMF_5 to 350L,
                        ToneGenerator.TONE_DTMF_7 to 400L
                    )
                    BgmStyle.NONE -> emptyList()
                }

                // Play loop gently with delays
                while (isActive && notes.isNotEmpty()) {
                    for ((tone, duration) in notes) {
                        if (!isActive) break
                        toneGen?.startTone(tone, duration.toInt())
                        delay(duration + 80L)
                    }
                    delay(1200L) // pause between melody phrases
                }
            } catch (e: Exception) {
                Log.e("AudioSfxEngine", "Error playing BGM", e)
            }
        }
    }

    fun stopBgm() {
        bgmJob?.cancel()
        bgmJob = null
    }

    fun release() {
        stopBgm()
        toneGen?.release()
        toneGen = null
    }
}
