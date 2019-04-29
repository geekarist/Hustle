package me.cpele.hustle.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import me.cpele.hustle.BuildConfig
import me.cpele.hustle.R

class BeeminderLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beeminder_login)

        val path = intent?.data?.toString()

        if (path?.startsWith(BuildConfig.BEEMINDER_REDIRECT_URI) == true) {
            val token = intent?.data?.getQueryParameter("access_token")
            val userName = intent?.data?.getQueryParameter("username")
            Toast.makeText(
                this,
                "Logged in as $userName, token is $token",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        } else {
            val uri = Uri.parse(BuildConfig.BEEMINDER_AUTH_ENDPOINT)
                .buildUpon()
                .appendQueryParameter("client_id", BuildConfig.BEEMINDER_CLIENT_ID)
                .appendQueryParameter("redirect_uri", BuildConfig.BEEMINDER_REDIRECT_URI)
                .appendQueryParameter("response_type", "token")
                .build()

            CustomTabsIntent.Builder()
                .build()
                .launchUrl(this, uri)
        }
    }

    private fun complete() {
        sendBroadcast(Intent(ACTION_BEEMINDER_LOGIN_SUCCESS))
        finish()
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, BeeminderLoginActivity::class.java))

        const val ACTION_BEEMINDER_LOGIN_SUCCESS = "me.cpele.hustle.ACTION_BEEMINDER_LOGIN_SUCCESS"
    }
}
