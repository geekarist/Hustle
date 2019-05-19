package me.cpele.hustle.app

import android.app.Application
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import me.cpele.hustle.domain.DataPointRepository
import retrofit2.Retrofit
import retrofit2.http.POST
import java.util.*

class BeeminderDataPointRepository(
    private val beeminderLogin: BeeminderLogin,
    private val application: Application
) : DataPointRepository {

    private val service = Retrofit.Builder()
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("https://www.beeminder.com")
        .build()
        .create(Service::class.java)

    interface Service {
        @POST("/api/v1/users/me/goals/{slug}/datapoints.json?auth_token={accessToken}&value={value}&comment={comment}")
        fun createDataPointAsync(
            accessToken: String,
            slug: String,
            value: Long,
            comment: String
        ): Deferred<DataPoint>
    }

    data class DataPoint(val id: String, val value: Long, val comment: String)

    override suspend fun insert(elapsedMillis: Long) {
        val authState = beeminderLogin.ensure()
        val inserted = service.createDataPointAsync(
            authState.accessToken,
            "hustle",
            elapsedMillis,
            makeComment()
        ).await()
        Log.d(javaClass.simpleName, "Data point inserted: $inserted")
    }

    override suspend fun findAll(): List<Long> {
        val authState = beeminderLogin.ensure()
        Log.d(
            javaClass.simpleName,
            "Login OK: user is ${authState.userName}, accessToken is ${authState.accessToken}"
        )
        return listOf()
    }

    private fun makeComment() =
        "via ${application.applicationInfo.loadLabel(application.packageManager)} at ${Date()}"
}
