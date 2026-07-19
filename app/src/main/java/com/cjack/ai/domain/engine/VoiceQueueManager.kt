package com.cjack.ai.domain.engine

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.PriorityBlockingQueue
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceQueueManager @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    data class VoiceMessage(val message: String, val isEmergency: Boolean = false) : Comparable<VoiceMessage> {
        override fun compareTo(other: VoiceMessage): Int {
            return other.isEmergency.compareTo(this.isEmergency)
        }
    }

    private val queue = PriorityBlockingQueue<VoiceMessage>()
    private val scope = CoroutineScope(Dispatchers.Default)
    private var queueJob: Job? = null

    private var lastSpokenMessage: String? = null
    private var lastSpokenTime: Long = 0
    private var isEnabled = true

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            isInitialized = true
            startQueueLoop()
        }
    }

    private fun startQueueLoop() {
        queueJob?.cancel()
        queueJob = scope.launch {
            while (true) {
                if (isEnabled && queue.isNotEmpty()) {
                    val next = queue.poll()
                    next?.let {
                        val words = it.message.split("\\s+".toRegex()).size
                        val duration = (words * 350L) + 1200L
                        
                        tts?.speak(it.message, TextToSpeech.QUEUE_FLUSH, null, "cjack_tts")
                        
                        lastSpokenMessage = it.message
                        lastSpokenTime = System.currentTimeMillis()
                        
                        // Wait for playback to finish + 5 seconds cooldown
                        delay(duration)
                        delay(5000L)
                    }
                } else {
                    delay(500)
                }
            }
        }
    }

    @Synchronized
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
        if (!enabled) {
            queue.clear()
            tts?.stop()
        }
    }

    @Synchronized
    fun queueMessage(message: String, isEmergency: Boolean = false) {
        if (!isEnabled || !isInitialized) return

        val now = System.currentTimeMillis()
        val prevWords = (lastSpokenMessage ?: "").split("\\s+".toRegex()).size
        val prevDuration = (prevWords * 350L) + 1200L
        
        // Cooldown check (ensure 5s delay after previous speech finishes)
        if ((now - lastSpokenTime) < (prevDuration + 5000L) && !isEmergency) {
            return
        }

        if (message == lastSpokenMessage && (now - lastSpokenTime) < 15000 && !isEmergency) {
            // Throttling identical messages within 15s
            return
        }

        if (isEmergency) {
            queue.clear()
            tts?.stop()
            startQueueLoop()
        }

        queue.add(VoiceMessage(message, isEmergency))
    }
    
    fun shutdown() {
        queueJob?.cancel()
        tts?.stop()
        tts?.shutdown()
    }
}
