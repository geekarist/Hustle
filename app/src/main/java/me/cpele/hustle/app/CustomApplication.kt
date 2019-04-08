package me.cpele.hustle.app

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.cpele.hustle.domain.EggTimer

class CustomApplication : Application() {

    private val androidStringProvider: EggTimer.StringProvider by lazy {
        AndroidStringProvider(this)
    }

    private val eggTimerFactory: EggTimer.Factory by lazy {
        EggTimer.Factory(androidStringProvider)
    }

    @ExperimentalCoroutinesApi
    val mainViewModelFactory by lazy {
        MainViewModel.Factory(eggTimerFactory)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CustomApplication
    }
}
