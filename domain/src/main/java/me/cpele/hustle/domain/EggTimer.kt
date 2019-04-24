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

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private var isPlaying: Boolean = false

    private val label: String
        get() = if (isPlaying) stringProvider.mainPause else stringProvider.mainPlay

    private var remainingMillis: Long = 0
    var elapsedMillis: Long = 0
        private set

    val hour: Int get() = TimeUnit.MILLISECONDS.toHours(remainingMillis).toInt()

    val minute: Int
        get() {
            val remainingHours = TimeUnit.MILLISECONDS.toHours(remainingMillis)
            val remainingHoursInMillis = TimeUnit.HOURS.toMillis(remainingHours)
            val remainingMinutesInMillis = remainingMillis - remainingHoursInMillis
            val remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(remainingMinutesInMillis)
            return remainingMinutes.toInt()
        }

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

    fun pause() {
        isPlaying = false
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
        private val timeFormatting: TimeFormatting
    ) {
        fun create(): EggTimer = EggTimer(stringProvider, timeFormatting)
    }
}
