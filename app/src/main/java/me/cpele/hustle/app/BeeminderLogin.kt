package me.cpele.hustle.app

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BeeminderLogin(private val application: Application) {

    var receiver: BroadcastReceiver? = null

    suspend fun ensure() {
        if (!isLoggedIn) completeLoginFlow()
    }

    private val isLoggedIn: Boolean get() = false

    private suspend fun completeLoginFlow() {
        try {
            withTimeout(TimeUnit.MINUTES.toMillis(2)) {
                suspendCoroutine { continuation: Continuation<Unit> ->
                    tearDownReceiver()
                    receiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context?, intent: Intent?) {
                            tearDownReceiver()
                            continuation.resume(Unit)
                        }
                    }
                    val filter = IntentFilter(BeeminderLoginActivity.ACTION_BEEMINDER_LOGIN_SUCCESS)
                    application.registerReceiver(receiver, filter)
                    BeeminderLoginActivity.start(application)
                }
            }
        } catch (e: TimeoutCancellationException) {
            tearDownReceiver()
            throw e
        }
    }

    private fun tearDownReceiver() {
        receiver?.let { application.unregisterReceiver(receiver) }
        receiver = null
    }

    companion object {
        private const val KEY_PREF_AUTH_STATE = "KEY_PREF_AUTH_STATE"
    }
}
