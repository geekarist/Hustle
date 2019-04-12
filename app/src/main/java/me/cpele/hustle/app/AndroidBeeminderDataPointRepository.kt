package me.cpele.hustle.app

import android.content.Context
import android.util.Log
import me.cpele.hustle.domain.DataPointRepository

class AndroidBeeminderDataPointRepository(private val context: Context) : DataPointRepository {
    override fun insert(elapsedMillis: Long) {
        Thread.sleep(3000)
        Log.d(javaClass.simpleName, "Data point sent: $elapsedMillis")
    }
}