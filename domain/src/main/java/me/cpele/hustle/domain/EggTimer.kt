package me.cpele.hustle.domain

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.coroutines.CoroutineContext

class EggTimer(private val stringProvider: StringProvider) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private var isPlaying: Boolean = false

    private val label: String
        get() = if (isPlaying) stringProvider.mainPause else stringProvider.mainPlay

    var remainingMillis: Long = 0

    @ExperimentalCoroutinesApi
    val channel: ReceiveChannel<State> = produce {
        val timer = Timer()
        invokeOnClose {
            timer.cancel()
        }
        val intervalMillis: Long = 1000
        val timerTask = timerTask {
            launch {
                if (isPlaying) remainingMillis -= intervalMillis
                val state = State(
                    remainingMillis.toString(),
                    isPlaying,
                    label
                )
                send(state)
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, intervalMillis)
        delay(Long.MAX_VALUE)
    }

    fun toggle() {
        isPlaying = !isPlaying
    }

    data class State(
        val timeStr: String,
        val isPlaying: Boolean,
        val playPauseLabel: String
    )

    interface StringProvider {
        val mainPause: String
        val mainPlay: String
    }

    class Factory(private val stringProvider: StringProvider) {
        fun create(): EggTimer = EggTimer(stringProvider)
    }
}
