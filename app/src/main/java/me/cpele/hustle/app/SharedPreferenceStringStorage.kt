package me.cpele.hustle.app

import android.app.Application
import android.preference.PreferenceManager
import me.cpele.hustle.domain.DataPointRepositorySupplier

class SharedPreferenceStringStorage(private val application: Application) :
    DataPointRepositorySupplier.StringStorage {

    override var value: String?
        set(newValue) {
            PreferenceManager.getDefaultSharedPreferences(application)
                .edit()
                .putString(KEY_DATA_POINT_TARGET, newValue)
                .apply()
        }
        get() =
            PreferenceManager.getDefaultSharedPreferences(application)
                .getString(KEY_DATA_POINT_TARGET, null)

    companion object {
        private const val KEY_DATA_POINT_TARGET = "KEY_DATA_POINT_TARGET"
    }
}
