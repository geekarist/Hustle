package me.cpele.hustle.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
            complete(token, userName)
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

    private fun complete(token: String?, userName: String?) {
        val intent = Intent(ACTION_BEEMINDER_LOGIN_SUCCESS)
        intent.putExtra(EXTRA_ACCESS_TOKEN, token)
        intent.putExtra(EXTRA_USER_NAME, userName)
        sendBroadcast(intent)
        finish()
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, BeeminderLoginActivity::class.java))

        const val ACTION_BEEMINDER_LOGIN_SUCCESS = "me.cpele.hustle.ACTION_BEEMINDER_LOGIN_SUCCESS"
        const val EXTRA_ACCESS_TOKEN = "EXTRA_ACCESS_TOKEN"
        const val EXTRA_USER_NAME = "EXTRA_USER_NAME"
    }
}
