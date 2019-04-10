package me.cpele.hustle.domain

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask
import kotlin.coroutines.CoroutineContext

class EggTimer(
    private val stringProvider: StringProvider,
    private val timeFormatting: TimeFormatting
) : CoroutineScope {

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
                    timeFormatting.apply(remainingMillis),
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

    fun reset() {
        remainingMillis = TimeUnit.MINUTES.toMillis(1)
        isPlaying = false
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

    interface TimeFormatting {
        fun apply(timeMillis: Long): String
    }

    class Factory(
        private val stringProvider: StringProvider,
        private val timeFormatting: TimeFormatting
    ) {
        fun create(): EggTimer = EggTimer(stringProvider, timeFormatting)
    }
}
