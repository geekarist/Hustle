package me.cpele.hustle.app

import android.text.format.DateUtils
import me.cpele.hustle.domain.EggTimer
import java.util.concurrent.TimeUnit

class AndroidTimeFormatting : EggTimer.TimeFormatting {
    override fun apply(timeMillis: Long): String =
        if (timeMillis < 0) {
            val positiveTimeMillis = -timeMillis
            val positiveTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(positiveTimeMillis)
            val positiveTimeStr = DateUtils.formatElapsedTime(positiveTimeSeconds)
            "-$positiveTimeStr"
        } else {
            DateUtils.formatElapsedTime(TimeUnit.MILLISECONDS.toSeconds(timeMillis))
        }
}
