package me.cpele.hustle.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.cpele.hustle.R

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _strTimeData =
        MutableLiveData<String>().apply { value = "00:00" }
    val strTimeData: LiveData<String> get() = _strTimeData

    private val isPlayingData = MutableLiveData<Boolean>().apply { value = false }

    private val _playPauseLabelData = MutableLiveData<String>()
    val playPauseLabelData: LiveData<String> get() = _playPauseLabelData

    fun onTogglePlayPause() {
        isPlayingData.value = isPlayingData.value != true
        val isPlaying = isPlayingData.value == true
        _playPauseLabelData.value =
            if (isPlaying) app.getString(R.string.main_pause)
            else app.getString(R.string.main_play)
    }
}
