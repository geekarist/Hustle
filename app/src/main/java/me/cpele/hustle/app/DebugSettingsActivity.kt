package me.cpele.hustle.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import me.cpele.hustle.R

class DebugSettingsActivity : Activity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DebugSettingsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_settings)
    }
}
