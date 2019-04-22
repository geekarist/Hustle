package me.cpele.hustle.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import me.cpele.hustle.R

class FirebaseLoginActivity : AppCompatActivity() {

    companion object {
        const val ACTION_FIREBASE_LOGIN_SUCCESS = "me.cpele.hustle.ACTION_FIREBASE_LOGIN_SUCCESS"
        private const val REQUEST_SIGN_IN = 42

        fun start(application: Application) =
            application.startActivity(Intent(application, FirebaseLoginActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_login)

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(
                listOf(
                    AuthUI.IdpConfig.GoogleBuilder().build()
                )
            ).build()

        startActivityForResult(intent, REQUEST_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser

                Log.d(javaClass.simpleName, "Logged in as $user")
                sendBroadcast(Intent(ACTION_FIREBASE_LOGIN_SUCCESS))
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
