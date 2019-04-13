package me.cpele.hustle.app

import android.content.Context
import kotlinx.coroutines.delay
import me.cpele.hustle.domain.DataPointRepository
import java.util.concurrent.TimeUnit

class AndroidFirebaseDataPointRepository(private val context: Context) : DataPointRepository {
    override suspend fun insert(elapsedMillis: Long) {
        // TODO: Login
        delay(3000)
        // TODO: Send to web service
        // TODO: Throw exception on error
        if (elapsedMillis < TimeUnit.SECONDS.toMillis(5)) throw Error()
    }
}