package me.cpele.hustle.app

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseLogin(private val application: Application) {

    suspend fun ensure() {
        if (!isLoggedIn()) completeLoginFlow()
    }

    private fun isLoggedIn(): Boolean =
        FirebaseAuth.getInstance().currentUser?.email != null

    private suspend fun completeLoginFlow() {
        var receiver: BroadcastReceiver? = null
        try {
            withTimeout(TimeUnit.MINUTES.toMillis(2)) {
                suspendCoroutine { continuation: Continuation<Unit> ->
                    receiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context?, intent: Intent?) {
                            Log.d(FirebaseLogin::class.java.simpleName, "Broadcast received")
                            application.unregisterReceiver(this)
                            continuation.resume(Unit)
                        }
                    }
                    val filter = IntentFilter(FirebaseLoginActivity.ACTION_FIREBASE_LOGIN_SUCCESS)
                    application.registerReceiver(receiver, filter)
                    Log.d(FirebaseLogin::class.java.simpleName, "Receiver registered")
                    FirebaseLoginActivity.start(application)
                }
            }
        } catch (e: TimeoutCancellationException) {
            receiver?.let { application.unregisterReceiver(receiver) }
            Log.d(FirebaseLogin::class.java.simpleName, "Timed out")
        }
    }
}
