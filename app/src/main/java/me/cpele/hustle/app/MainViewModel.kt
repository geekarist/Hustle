package me.cpele.hustle.app

import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import me.cpele.hustle.domain.EggTimer

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
        viewModelScope.launch {
            for (state in eggTimer.channel) {
                _strTimeData.value = state.timeStr
                isPlayingData.value = state.isPlaying
                _playPauseLabelData.value = state.playPauseLabel
            }
        }
    }

    fun onTogglePlayPause() = eggTimer.toggle()

    override fun onCleared() {
        super.onCleared()
        eggTimer.channel.cancel()
    }

    class Factory(
        private val eggTimerFactory: EggTimer.Factory
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(MainViewModel(eggTimerFactory)) as T
        }
    }
}
