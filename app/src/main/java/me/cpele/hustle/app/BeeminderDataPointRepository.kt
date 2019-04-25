package me.cpele.hustle.app

import me.cpele.hustle.domain.DataPointRepository

class BeeminderDataPointRepository(private val beeminderLogin: BeeminderLogin) :
    DataPointRepository {

    override suspend fun insert(elapsedMillis: Long) {
        beeminderLogin.ensure()
    }

    override suspend fun findAll(): List<Long> {
        beeminderLogin.ensure()
        return listOf()
    }
}
