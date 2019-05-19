package me.cpele.hustle.app

import android.app.Application
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import me.cpele.hustle.domain.DataPointRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

class BeeminderDataPointRepository(
    private val beeminderLogin: BeeminderLogin,
    private val application: Application
) : DataPointRepository {

    private val service = Retrofit.Builder()
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://www.beeminder.com")
        .build()
        .create(Service::class.java)

    interface Service {
        @POST("/api/v1/users/me/goals/{slug}/datapoints.json")
        fun createDataPointAsync(
            @Path("slug") slug: String,
            @Query("access_token") accessToken: String,
            @Query("value") value: Long,
            @Query("comment") comment: String
        ): Deferred<DataPoint>
    }

    data class DataPoint(val id: String, val value: Long, val comment: String)

    override suspend fun insert(elapsedMillis: Long) {
        val authState = beeminderLogin.ensure()
        val inserted = service.createDataPointAsync(
            "hustle",
            authState.accessToken,
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
