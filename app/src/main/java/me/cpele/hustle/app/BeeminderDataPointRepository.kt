package me.cpele.hustle.app

import android.util.Log
import me.cpele.hustle.domain.DataPointRepository

class BeeminderDataPointRepository(private val beeminderLogin: BeeminderLogin) :
    DataPointRepository {

    override suspend fun insert(elapsedMillis: Long) {
        val authState = beeminderLogin.ensure()
        Log.d(
            javaClass.simpleName,
            "Login OK: user is ${authState.userName}, accessToken is ${authState.accessToken}"
        )
    }

    override suspend fun findAll(): List<Long> {
        val authState = beeminderLogin.ensure()
        Log.d(
            javaClass.simpleName,
            "Login OK: user is ${authState.userName}, accessToken is ${authState.accessToken}"
        )
        return listOf()
    }
}
