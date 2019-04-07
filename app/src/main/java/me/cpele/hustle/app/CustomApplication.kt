package me.cpele.hustle.app

import android.app.Application
import me.cpele.hustle.domain.TogglePlayPauseUseCase

class CustomApplication : Application() {

    private val androidStringProvider: TogglePlayPauseUseCase.StringProvider by lazy {
        AndroidStringProvider(this)
    }

    private val togglePlayPauseUseCase: TogglePlayPauseUseCase by lazy {
        TogglePlayPauseUseCase(androidStringProvider)
    }

    val mainViewModelFactory by lazy {
        MainViewModel.Factory(togglePlayPauseUseCase)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CustomApplication
    }
}
