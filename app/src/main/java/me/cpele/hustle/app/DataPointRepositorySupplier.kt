package me.cpele.hustle.app

import me.cpele.hustle.domain.DataPointRepository
import me.cpele.hustle.domain.InMemoryDataPointRepository

class DataPointRepositorySupplier(
    firebaseDataPointRepository: DataPointRepository,
    inMemoryDataPointRepository: InMemoryDataPointRepository
) {
    fun get(): DataPointRepository {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun switchTo(target: DataPointTarget) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
