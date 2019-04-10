package me.cpele.hustle.app

import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import me.cpele.hustle.domain.EggTimer
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class MainViewModel(eggTimerFactory: EggTimer.Factory) : ViewModel() {

    private val eggTimer = eggTimerFactory.create()

    private val _strTimeData =
        MutableLiveData<String>().apply { value = "00:00" }
    val strTimeData: LiveData<String> get() = _strTimeData

    private val isPlayingData = MutableLiveData<Boolean>().apply { value = false }

    private val _playPauseLabelData = MutableLiveData<String>()
    val playPauseLabelData: LiveData<String> get() = _playPauseLabelData

    init {
        eggTimer.remainingMillis = TimeUnit.MINUTES.toMillis(1)

        viewModelScope.launch {
            for (state in eggTimer.channel) {
                _strTimeData.value = state.timeStr
                isPlayingData.value = state.isPlaying
                _playPauseLabelData.value = state.playPauseLabel
            }
        }
    }

    fun onTogglePlayPause() = eggTimer.toggle()
    fun onReset() = eggTimer.reset()

    override fun onCleared() {
        eggTimer.channel.cancel()
        super.onCleared()
    }

    class Factory(
        private val eggTimerFactory: EggTimer.Factory
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(MainViewModel(eggTimerFactory)) as T
        }
    }
}
