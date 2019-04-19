package me.cpele.hustle.app

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class FirebaseLogin : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun complete() {
        TODO()
    }
}
