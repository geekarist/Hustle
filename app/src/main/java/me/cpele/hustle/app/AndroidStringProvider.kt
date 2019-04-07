package me.cpele.hustle.app

import me.cpele.hustle.R
import me.cpele.hustle.domain.TogglePlayPauseUseCase

class AndroidStringProvider(
    private val customApplication: CustomApplication
) : TogglePlayPauseUseCase.StringProvider {

    override val mainPause: String get() = customApplication.getString(R.string.main_pause)
    override val mainPlay: String get() = customApplication.getString(R.string.main_play)
}
