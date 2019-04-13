package me.cpele.hustle.domain

interface DataPointRepository {
    suspend fun insert(elapsedMillis: Long)
}
