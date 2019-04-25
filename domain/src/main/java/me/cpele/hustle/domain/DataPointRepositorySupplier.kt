package me.cpele.hustle.domain

class DataPointRepositorySupplier(
    private val stringStorage: StringStorage,
    vararg repositoryArgs: Pair<DataPointTarget, DataPointRepository>
) {
    private val repositories: MutableMap<DataPointTarget, DataPointRepository> = mutableMapOf()

    init {
        repositoryArgs.forEach { repositories += it.first to it.second }
        switchTo(repositoryArgs[0].first)
    }

    fun get(): DataPointRepository {
        return stringStorage.value
            ?.let { DataPointTarget.valueOf(it) }
            ?.let { repositories[it] }
            ?: TODO()
    }

    fun switchTo(target: DataPointTarget) {
        stringStorage.value = target.name
    }

    interface StringStorage {
        var value: String?
    }
}
