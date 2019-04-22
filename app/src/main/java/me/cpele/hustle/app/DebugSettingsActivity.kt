package me.cpele.hustle.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_debug_settings.*
import me.cpele.hustle.R

class DebugSettingsActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DebugSettingsActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            CustomApplication.instance.debugViewModelFactory
        ).get(DebugSettingsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = DataPointAdapter()
        debug_data_points_list.adapter = adapter
        debug_data_points_list.layoutManager = LinearLayoutManager(this)

        viewModel.viewStateData.observe(this, Observer {
            adapter.submitList(it.dataPoints)
            debug_data_points_list.visibility = it.dataPointsVisibility
            debug_data_points_error_text.visibility = it.dataPointsErrorVisibility
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
