package me.cpele.hustle.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import me.cpele.hustle.R

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _strTimeData =
        MutableLiveData<String>().apply { value = "00:00" }
    val strTimeData: LiveData<String> get() = _strTimeData

    private val isPlayingData = MutableLiveData<Boolean>().apply { value = false }

    val playPauseLabelData: LiveData<String> = Transformations.map(isPlayingData) {
        if (it) application.getString(R.string.main_pause)
        else application.getString(R.string.main_play)
    }

    fun onTogglePlayPause() {
        isPlayingData.value = isPlayingData.value != true
    }
}
