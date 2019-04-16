package me.cpele.hustle.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DebugSettingsViewModel : ViewModel() {

    private val _dataPoints = MutableLiveData<List<Long>>().apply {
        value = listOf(
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )
    }
    val dataPoints: LiveData<List<Long>> = _dataPoints
}
