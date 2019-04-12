package me.cpele.hustle.app

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.cpele.hustle.domain.EggTimer

@ExperimentalCoroutinesApi
class MainViewModel(eggTimerFactory: EggTimer.Factory) : ViewModel() {

    private val eggTimer = eggTimerFactory.create()

    private val _strTimeData = MutableLiveData<String>()
    val strTimeData: LiveData<String> get() = _strTimeData

    private val isPlayingData = MutableLiveData<Boolean>()

    private val _playPauseLabelData = MutableLiveData<String>()
    val playPauseLabelData: LiveData<String> get() = _playPauseLabelData

    private val _elapsedTimeStr = MutableLiveData<String>()
    val elapsedTimeStr: LiveData<String> = _elapsedTimeStr

    private val _dataPointSentEvent = MutableLiveData<LiveEvent<Unit>>()
    val dataPointSentEvent: LiveData<LiveEvent<Unit>> = _dataPointSentEvent

    init {
        viewModelScope.launch {
            for (state in eggTimer.channel) {
                _strTimeData.value = state.timeStr
                isPlayingData.value = state.isPlaying
                _playPauseLabelData.value = state.playPauseLabel
                _elapsedTimeStr.value = state.elapsedTimeStr
            }
        }
    }

    fun onTogglePlayPause() = eggTimer.toggle()
    fun onReset() = eggTimer.reset()

    override fun onCleared() {
        eggTimer.cancel()
        super.onCleared()
    }

    fun onChangeDuration(hour: Int, minute: Int) {
        eggTimer.changeDuration(hour, minute)
    }

    fun sendDataPoint() = viewModelScope.launch(Dispatchers.IO) {
        eggTimer.send()
        _dataPointSentEvent.postValue(LiveEvent(Unit))
    }

    class Factory(
        private val eggTimerFactory: EggTimer.Factory
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(MainViewModel(eggTimerFactory)) as T
        }
    }
}
