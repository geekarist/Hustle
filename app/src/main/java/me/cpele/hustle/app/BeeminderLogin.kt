package me.cpele.hustle.app

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class BeeminderLogin(private val application: Application) {

    suspend fun ensure() {
        if (!isLoggedIn()) completeLoginFlow()
    }

    private fun isLoggedIn(): Boolean = false

    private suspend fun completeLoginFlow() {
        var receiver: BroadcastReceiver? = null
        try {
            withTimeout(TimeUnit.MINUTES.toMillis(2)) {
                suspendCancellableCoroutine { continuation: Continuation<Unit> ->
                    receiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context?, intent: Intent?) {
                            Log.d(BeeminderLogin::class.java.simpleName, "Broadcast received")
                            application.unregisterReceiver(this)
                            continuation.resume(Unit)
                        }
                    }
                    val filter = IntentFilter(BeeminderLoginActivity.ACTION_BEEMINDER_LOGIN_SUCCESS)
                    application.registerReceiver(receiver, filter)
                    Log.d(BeeminderLogin::class.java.simpleName, "Receiver registered")
                    BeeminderLoginActivity.start(application)
                }
            }
        } catch (e: TimeoutCancellationException) {
            receiver?.let { application.unregisterReceiver(receiver) }
            Log.d(BeeminderLogin::class.java.simpleName, "Timed out")
        }
    }
}
