package me.cpele.hustle.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.cpele.hustle.domain.TogglePlayPauseUseCase

class MainViewModel(
    private val togglePlayPauseUseCase: TogglePlayPauseUseCase
) : ViewModel() {

    private val _strTimeData =
        MutableLiveData<String>().apply { value = "00:00" }
    val strTimeData: LiveData<String> get() = _strTimeData

    private val isPlayingData = MutableLiveData<Boolean>().apply { value = false }

    private val _playPauseLabelData = MutableLiveData<String>()
    val playPauseLabelData: LiveData<String> get() = _playPauseLabelData

    fun onTogglePlayPause() {
        val wasPlaying = isPlayingData.value == true

        val request = TogglePlayPauseUseCase.Request(wasPlaying)
        val response = togglePlayPauseUseCase.execute(request)

        isPlayingData.value = response.playing
        _playPauseLabelData.value = response.label
    }

    class Factory(
        private val togglePlayPauseUseCase: TogglePlayPauseUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(MainViewModel(togglePlayPauseUseCase)) as T
        }
    }
}
