package me.cpele.hustle.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.cpele.hustle.R
import java.util.concurrent.Executors

class BeeminderLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beeminder_login)

        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            Thread.sleep(5000)
            runOnUiThread {
                sendBroadcast(Intent(ACTION_BEEMINDER_LOGIN_SUCCESS))
                finish()
            }
        }
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, BeeminderLoginActivity::class.java))

        const val ACTION_BEEMINDER_LOGIN_SUCCESS = "me.cpele.hustle.ACTION_BEEMINDER_LOGIN_SUCCESS"
    }
}
