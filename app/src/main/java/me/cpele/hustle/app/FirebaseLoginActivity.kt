package me.cpele.hustle.app

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executors

class FirebaseLoginActivity : AppCompatActivity() {

    companion object {
        const val ACTION_FIREBASE_LOGIN_SUCCESS = "me.cpele.hustle.ACTION_FIREBASE_LOGIN_SUCCESS"

        fun start(application: Application) =
            application.startActivity(Intent(application, FirebaseLoginActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply { text = "Fake Firebase login" })
        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            runOnUiThread {
                Toast.makeText(
                    application,
                    "Please wait while logging in...",
                    Toast.LENGTH_SHORT
                ).show()
            }
            Thread.sleep(20000)
            runOnUiThread {
                Toast.makeText(
                    application,
                    "OK, logged in...",
                    Toast.LENGTH_SHORT
                ).show()
                sendBroadcast(Intent(ACTION_FIREBASE_LOGIN_SUCCESS))
                finish()
            }
        }
    }
}
