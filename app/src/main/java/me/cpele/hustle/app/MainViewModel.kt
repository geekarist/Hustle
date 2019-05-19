package me.cpele.hustle.app

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.cpele.hustle.domain.EggTimer
import me.cpele.hustle.domain.SendDataPointUseCase

class MainViewModel(
    eggTimerFactory: EggTimer.Factory,
    private val sendDataPointUseCase: SendDataPointUseCase
) : ViewModel() {

    private val eggTimer = eggTimerFactory.create()

    private val _viewStateData = MutableLiveData<ViewState>()
    val viewStateData: LiveData<ViewState> = _viewStateData

    private val _viewEventData = MutableLiveData<Consumable<ViewEvent>>()
    val viewEventData: LiveData<Consumable<ViewEvent>> = _viewEventData

    init {
        viewModelScope.launch {
            @Suppress("EXPERIMENTAL_API_USAGE")
            for (state in eggTimer.channel) {
                _viewStateData.postValue(
                    ViewState(
                        state.timeStr,
                        state.isPlaying,
                        state.playPauseLabel,
                        state.elapsedTimeStr
                    )
                )
            }
        }
    }

    fun onTogglePlayPause() = eggTimer.toggle()
    fun onReset() = eggTimer.reset()

    override fun onCleared() {
        @Suppress("EXPERIMENTAL_API_USAGE")
        eggTimer.cancel()
        super.onCleared()
    }

    fun onChangeDuration(hour: Int, minute: Int) = eggTimer.changeDuration(hour, minute)

    fun sendDataPoint() = viewModelScope.launch {
        try {
            val response = sendDataPointUseCase.execute(eggTimer)
            _viewEventData.postValue(Consumable(ViewEvent.Message(response.message)))
        } catch (t: Throwable) {
            _viewEventData.postValue(Consumable(ViewEvent.Message(t.message)))
            Log.w(javaClass.simpleName, t)
        }
    }

    fun onClickChange() =
        _viewEventData.postValue(
            Consumable(
                ViewEvent.PickTime(
                    eggTimer.hour,
                    eggTimer.minute
                )
            )
        )

    data class ViewState(
        val remainingTime: String,
        val isPlaying: Boolean,
        val playPauseLabel: String,
        val elapsedTime: String
    )

    sealed class ViewEvent {
        data class Message(val message: String?) : ViewEvent()
        data class PickTime(val hour: Int, val minute: Int) : ViewEvent()
    }

    class Factory(
        private val eggTimerFactory: EggTimer.Factory,
        private val sendDataPointUseCase: SendDataPointUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(MainViewModel(eggTimerFactory, sendDataPointUseCase)) as T
        }
    }
}
