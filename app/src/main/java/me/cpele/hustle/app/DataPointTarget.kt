package me.cpele.hustle.app

enum class DataPointTarget(private val str: String) {

    FIREBASE("Firebase"),
    IN_MEMORY("In memory");

    companion object {
        fun of(str: CharSequence?) = values().first { it.str == str }
    }
}
