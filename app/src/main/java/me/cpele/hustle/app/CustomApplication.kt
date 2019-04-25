package me.cpele.hustle.app

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.cpele.hustle.domain.*

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

    private val stringStorage: DataPointRepositorySupplier.StringStorage by lazy {
        SharedPreferenceStringStorage(this)
    }

    private val dataPointRepositorySupplier: DataPointRepositorySupplier by lazy {
        DataPointRepositorySupplier(
            stringStorage,
            DataPointTarget.FIREBASE to AndroidFirebaseDataPointRepository(firebaseLogin),
            DataPointTarget.IN_MEMORY to InMemoryDataPointRepository()
        )
    }

    private val eggTimerFactory: EggTimer.Factory by lazy {
        EggTimer.Factory(
            androidStringProvider,
            androidTimeFormatting
        )
    }

    private val sendDataPointUseCase: SendDataPointUseCase by lazy {
        SendDataPointUseCase(dataPointRepositorySupplier)
    }

    @ExperimentalCoroutinesApi
    val mainViewModelFactory by lazy {
        MainViewModel.Factory(eggTimerFactory, sendDataPointUseCase)
    }

    val debugViewModelFactory by lazy {
        DebugSettingsViewModel.Factory(dataPointRepositorySupplier)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CustomApplication
    }
}
