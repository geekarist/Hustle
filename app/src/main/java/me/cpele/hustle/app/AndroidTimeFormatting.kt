package me.cpele.hustle.app

import android.text.format.DateUtils
import me.cpele.hustle.domain.EggTimer
import java.util.concurrent.TimeUnit

class AndroidTimeFormatting : EggTimer.TimeFormatting {
    override fun apply(timeMillis: Long): String =
        DateUtils.formatElapsedTime(TimeUnit.MILLISECONDS.toSeconds(timeMillis))
}
