package me.cpele.hustle.app

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        viewModel.viewStateData.observe(this, Observer { it.render() })
        viewModel.viewEventData.observe(this, Observer { it.render() })

        main_play_pause_button.setOnClickListener { viewModel.onTogglePlayPause() }
        main_reset_button.setOnClickListener { viewModel.onReset() }

        main_change_button.setOnClickListener { viewModel.onClickChange() }

        main_send_button.setOnClickListener {
            val msgSendingDataPoint = getString(
                R.string.main_sending_data_point,
                viewModel.viewStateData.value?.elapsedTime
            )
            Snackbar.make(main_container, msgSendingDataPoint, Snackbar.LENGTH_SHORT).show()
            viewModel.sendDataPoint()
        }
    }

    private fun Consumable<MainViewModel.ViewEvent>.render() {
        when (val event = unconsumed) {
            is MainViewModel.ViewEvent.Message -> event.render()
            is MainViewModel.ViewEvent.PickTime -> event.render()
        }
    }

    private fun MainViewModel.ViewEvent.Message.render() {
        val msgDataPointSent = getString(R.string.main_data_point_sent, message)
        Snackbar.make(main_container, msgDataPointSent, Snackbar.LENGTH_SHORT).show()
    }

    private fun MainViewModel.ViewEvent.PickTime.render() {
        TimePickerDialog(
            this@MainActivity,
            { _, hourOfDay, minute ->
                viewModel.onChangeDuration(hourOfDay, minute)
            },
            hour,
            minute,
            true
        ).show()
    }

    private fun MainViewModel.ViewState.render() {
        main_display_text.text = remainingTime
        main_play_pause_button.text = playPauseLabel
        main_send_button.text = getString(R.string.main_send_elapsed, elapsedTime)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when (item?.itemId) {
            R.id.item_debug_settings -> {
                DebugSettingsActivity.start(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
