package me.cpele.hustle.app

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.cpele.hustle.R

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            CustomApplication.instance.mainViewModelFactory
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

        viewModel.elapsedTimeStr.observe(this, Observer {
            main_send_button.text = getString(R.string.main_send_elapsed, it)
        })

        viewModel.dataPointSentEvent.observe(this, Observer {
            it.unconsumed?.apply {
                val msgDataPointSent = getString(
                    R.string.main_data_point_sent,
                    viewModel.elapsedTimeStr.value
                )
                Snackbar.make(main_container, msgDataPointSent, Snackbar.LENGTH_SHORT).show()
            }
        })

        main_play_pause_button.setOnClickListener { viewModel.onTogglePlayPause() }
        main_reset_button.setOnClickListener { viewModel.onReset() }
        main_change_button.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    viewModel.onChangeDuration(hourOfDay, minute)
                },
                0,
                0,
                true
            ).show()
        }

        main_send_button.setOnClickListener {
            val msgSendingDataPoint = getString(
                R.string.main_sending_data_point,
                viewModel.elapsedTimeStr.value
            )
            Snackbar.make(main_container, msgSendingDataPoint, Snackbar.LENGTH_SHORT).show()
            viewModel.sendDataPoint()
        }
    }
}
