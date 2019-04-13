package me.cpele.hustle.app

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.cpele.hustle.domain.DataPointRepository
import me.cpele.hustle.domain.EggTimer
import me.cpele.hustle.domain.SendDataPointUseCase

class CustomApplication : Application() {

    private val androidStringProvider: EggTimer.StringProvider by lazy {
        AndroidStringProvider(this)
    }

    private val androidTimeFormatting: EggTimer.TimeFormatting by lazy {
        AndroidTimeFormatting()
    }

    private val firebaseDataPointRepository: DataPointRepository by lazy {
        AndroidFirebaseDataPointRepository()
    }

    private val eggTimerFactory: EggTimer.Factory by lazy {
        EggTimer.Factory(
            androidStringProvider,
            androidTimeFormatting
        )
    }

    private val sendDataPointUseCase: SendDataPointUseCase by lazy {
        SendDataPointUseCase(firebaseDataPointRepository)
    }

    @ExperimentalCoroutinesApi
    val mainViewModelFactory by lazy {
        MainViewModel.Factory(eggTimerFactory, sendDataPointUseCase)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CustomApplication
    }
}
