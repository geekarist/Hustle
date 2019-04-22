package me.cpele.hustle.app

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.cpele.hustle.domain.DataPointRepository

class DebugSettingsViewModel(
    private val dataPointRepository: DataPointRepository,
    private val firebaseLogin: FirebaseLogin
) : ViewModel() {

    private val _viewStateData = MutableLiveData<ViewState>()
    val viewStateData: LiveData<ViewState> = _viewStateData

    private val _viewEventData = MutableLiveData<Consumable<ViewEvent>>()
    val viewEventData: LiveData<Consumable<ViewEvent>> = _viewEventData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dataPoints = dataPointRepository.findAll()
                _viewStateData.postValue(
                    ViewState(
                        dataPoints = dataPoints,
                        dataPointsVisibility = View.VISIBLE,
                        dataPointsErrorVisibility = View.GONE
                    )
                )
            } catch (e: Error) {
                _viewStateData.postValue(
                    ViewState(
                        dataPointsVisibility = View.GONE,
                        dataPointsErrorVisibility = View.VISIBLE
                    )
                )
            }
        }
    }

    fun onFirebaseLoginClicked() = viewModelScope.launch {
        try {
            firebaseLogin.complete()
        } catch (e: Error) {
            val message = "Error logging in to Firebase: ${e.message}"
            _viewEventData.postValue(Consumable(ViewEvent.Toast(message)))
        }
    }

    data class ViewState(
        val dataPoints: List<Long> = emptyList(),
        val dataPointsVisibility: Int,
        val dataPointsErrorVisibility: Int
    )

    sealed class ViewEvent {
        data class Toast(val message: String) : ViewEvent()
    }

    class Factory(
        private val dataPointRepository: DataPointRepository,
        private val firebaseLogin: FirebaseLogin
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(DebugSettingsViewModel(dataPointRepository, firebaseLogin)) as T
        }
    }
}
