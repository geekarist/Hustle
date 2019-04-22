package me.cpele.hustle.app

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.cpele.hustle.domain.DataPointRepository

class DebugSettingsViewModel(private val dataPointRepository: DataPointRepository) : ViewModel() {

    private val _dataPointsData = MutableLiveData<List<Long>>()
    val dataPointsData: LiveData<List<Long>> = _dataPointsData

    private val _dataPointsVisibilityData = MutableLiveData<Int>()
    val dataPointsVisibilityData: LiveData<Int> = _dataPointsVisibilityData

    private val _dataPointsErrorVisibilityData = MutableLiveData<Int>()
    val dataPointsErrorVisibilityData: LiveData<Int> = _dataPointsErrorVisibilityData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dataPoints = dataPointRepository.findAll()
                _dataPointsData.postValue(dataPoints)
                _dataPointsVisibilityData.postValue(View.VISIBLE)
                _dataPointsErrorVisibilityData.postValue(View.GONE)
            } catch (e: Error) {
                _dataPointsVisibilityData.postValue(View.GONE)
                _dataPointsErrorVisibilityData.postValue(View.VISIBLE)
            }
        }
    }

    class Factory(private val dataPointRepository: DataPointRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(DebugSettingsViewModel(dataPointRepository)) as T
        }
    }
}
