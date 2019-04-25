package me.cpele.hustle.app

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.cpele.hustle.domain.DataPointRepositorySupplier
import me.cpele.hustle.domain.DataPointTarget

class DebugSettingsViewModel(
    private val dataPointRepositorySupplier: DataPointRepositorySupplier
) : ViewModel() {

    private val _viewStateData = MutableLiveData<ViewState>()
    val viewStateData: LiveData<ViewState> = _viewStateData

    private val _viewEventData = MutableLiveData<Consumable<ViewEvent>>()
    val viewEventData: LiveData<Consumable<ViewEvent>> = _viewEventData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dataPoints = dataPointRepositorySupplier.get().findAll()
                _viewStateData.postValue(
                    ViewState(
                        dataPoints = dataPoints,
                        dataPointsVisibility = View.VISIBLE,
                        dataPointsErrorVisibility = View.GONE
                    )
                )
            } catch (t: Throwable) {
                _viewStateData.postValue(
                    ViewState(
                        dataPointsVisibility = View.GONE,
                        dataPointsErrorVisibility = View.VISIBLE
                    )
                )
            }
        }
    }

    fun onTargetSelected(target: CharSequence?) =
        dataPointRepositorySupplier.switchTo(DataPointTarget.of(target))

    data class ViewState(
        val dataPoints: List<Long> = emptyList(),
        val dataPointsVisibility: Int,
        val dataPointsErrorVisibility: Int
    )

    sealed class ViewEvent {
        data class Toast(val message: String) : ViewEvent()
    }

    class Factory(
        private val dataPointRepositorySupplier: DataPointRepositorySupplier
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(DebugSettingsViewModel(dataPointRepositorySupplier)) as T
        }
    }
}
