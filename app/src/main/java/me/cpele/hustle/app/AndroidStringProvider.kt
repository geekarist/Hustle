package me.cpele.hustle.app

import android.app.Application
import me.cpele.hustle.R
import me.cpele.hustle.domain.EggTimer

class AndroidStringProvider(
    private val customApplication: Application
) : EggTimer.StringProvider {

    override val mainPause: String get() = customApplication.getString(R.string.main_pause)
    override val mainPlay: String get() = customApplication.getString(R.string.main_play)
}
