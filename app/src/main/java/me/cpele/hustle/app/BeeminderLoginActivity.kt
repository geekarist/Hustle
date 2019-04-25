package me.cpele.hustle.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executors

class BeeminderLoginActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = TextView(this).apply { text = "Allegedly logging in to Beeminder..." }
        setContentView(view)

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
