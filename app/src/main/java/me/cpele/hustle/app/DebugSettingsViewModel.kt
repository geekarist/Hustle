package me.cpele.hustle.app

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.cpele.hustle.domain.DataPointRepository

class DebugSettingsViewModel(private val dataPointRepository: DataPointRepository) : ViewModel() {

    private val _dataPointsData = MutableLiveData<List<Long>>()
    val dataPointsData: LiveData<List<Long>> = _dataPointsData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val dataPoints = dataPointRepository.findAll()
            _dataPointsData.postValue(dataPoints)
        }
    }

    class Factory(private val dataPointRepository: DataPointRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(DebugSettingsViewModel(dataPointRepository)) as T
        }
    }
}
