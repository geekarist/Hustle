package me.cpele.hustle.app

import android.app.Application
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

    private val firebaseLogin: FirebaseLogin by lazy {
        FirebaseLogin(this)
    }

    private val beeminderLogin by lazy {
        BeeminderLogin(this)
    }

    private val dataPointRepository: DataPointRepository by lazy {
        BeeminderDataPointRepository(beeminderLogin, this)
//        AndroidFirebaseDataPointRepository(firebaseLogin)
    }

    private val eggTimerFactory: EggTimer.Factory by lazy {
        EggTimer.Factory(
            androidStringProvider,
            androidTimeFormatting
        )
    }

    private val sendDataPointUseCase: SendDataPointUseCase by lazy {
        SendDataPointUseCase(dataPointRepository)
    }

    val mainViewModelFactory by lazy {
        MainViewModel.Factory(eggTimerFactory, sendDataPointUseCase)
    }

    val debugViewModelFactory by lazy {
        DebugSettingsViewModel.Factory(dataPointRepository, firebaseLogin)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CustomApplication
    }
}
