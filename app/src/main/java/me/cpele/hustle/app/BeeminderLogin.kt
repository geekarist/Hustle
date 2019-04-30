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
import kotlin.coroutines.suspendCoroutine

class BeeminderLogin(private val application: Application) {

    var receiver: BroadcastReceiver? = null

    suspend fun ensure(): AuthState = if (!isLoggedIn) completeLoginFlow() else TODO()

    private val isLoggedIn: Boolean get() = false

    private suspend fun completeLoginFlow(): AuthState {
        try {
            return withTimeout(TimeUnit.MINUTES.toMillis(2)) {
                suspendCoroutine { continuation: Continuation<AuthState> ->
                    tearDownReceiver()
                    receiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context?, intent: Intent?) {

                            tearDownReceiver()

                            val token =
                                intent?.getStringExtra(BeeminderLoginActivity.EXTRA_ACCESS_TOKEN)
                            val userName =
                                intent?.getStringExtra(BeeminderLoginActivity.EXTRA_USER_NAME)

                            continuation.resumeWith(
                                if (token != null && userName != null) {
                                    Result.success(AuthState(userName, token))
                                } else {
                                    Result.failure(
                                        IllegalStateException(
                                            "Authentication didn't bring a valid accessToken or user name"
                                        )
                                    )
                                }
                            )
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

    data class AuthState(val userName: String, val accessToken: String)

    companion object {
        private const val KEY_PREF_AUTH_STATE = "KEY_PREF_AUTH_STATE"
    }
}
