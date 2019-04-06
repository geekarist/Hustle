package me.cpele.hustle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.strTimeData.observe(this, Observer {
            main_display_text.text = it
        })

        viewModel.playPauseLabelData.observe(this, Observer {
            main_play_pause_button.text = it
        })

        main_play_pause_button.setOnClickListener {
            viewModel.onTogglePlayPause()
        }
    }
}
