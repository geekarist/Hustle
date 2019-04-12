package me.cpele.hustle.domain

interface DataPointRepository {
    fun insert(elapsedMillis: Long)
}
