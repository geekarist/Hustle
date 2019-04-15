package me.cpele.hustle.domain

class InMemoryDataPointRepository : DataPointRepository {

    private val dataPoints = mutableListOf<Long>()

    override suspend fun insert(elapsedMillis: Long) {
        dataPoints += elapsedMillis
    }
}