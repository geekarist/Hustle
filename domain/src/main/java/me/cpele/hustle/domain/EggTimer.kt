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
    private val timeFormatting: TimeFormatting,
    private val dataPointRepository: DataPointRepository
) : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private var isPlaying: Boolean = false

    private val label: String
        get() = if (isPlaying) stringProvider.mainPause else stringProvider.mainPlay

    private var remainingMillis: Long = 0
    private var elapsedMillis: Long = 0

    @ExperimentalCoroutinesApi
    val channel: ReceiveChannel<State> = produce {
        val timer = Timer()
        invokeOnClose {
            timer.cancel()
        }
        val intervalMillis: Long = 1000
        val timerTask = timerTask {
            launch {
                if (isPlaying) {
                    remainingMillis -= intervalMillis
                    elapsedMillis += intervalMillis
                }
                val state = State(
                    timeFormatting.apply(remainingMillis),
                    isPlaying,
                    label,
                    timeFormatting.apply(elapsedMillis)
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
        remainingMillis = 0
        elapsedMillis = 0
        isPlaying = false
    }

    fun changeDuration(hourOfDay: Int, minute: Int) {
        remainingMillis =
            TimeUnit.HOURS.toMillis(hourOfDay.toLong()) + TimeUnit.MINUTES.toMillis(minute.toLong())
    }

    fun send() {
        isPlaying = false
        dataPointRepository.insert(elapsedMillis)
    }

    data class State(
        val timeStr: String,
        val isPlaying: Boolean,
        val playPauseLabel: String,
        val elapsedTimeStr: String
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
        private val timeFormatting: TimeFormatting,
        private val dataPointRepository: DataPointRepository
    ) {
        fun create(): EggTimer = EggTimer(stringProvider, timeFormatting, dataPointRepository)
    }
}
