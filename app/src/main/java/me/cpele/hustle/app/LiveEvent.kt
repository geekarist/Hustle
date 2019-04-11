package me.cpele.hustle.app

class LiveEvent<T>(private val value: T) {

    private var isConsumed: Boolean = false

    val unconsumed: T?
        get() {
            synchronized(this) {
                val result = if (isConsumed) null else value
                isConsumed = true
                return result
            }
        }
}
