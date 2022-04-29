package com.el3asas.eduapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Bundle
import com.el3asas.eduapp.ui.WakeAlarmActivity
import androidx.databinding.DataBindingUtil
import com.el3asas.eduapp.R
import android.view.WindowManager
import android.content.Intent
import android.os.Build
import android.view.View
import com.el3asas.eduapp.background.PlayerService
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.el3asas.eduapp.databinding.ActivityWakeAlarmBinding

class WakeAlarmActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        close.value = false
        val binding: ActivityWakeAlarmBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_wake_alarm)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        val requestCode = intent.getIntExtra("requestCode", 0)
        var prayName = ""
        when (requestCode) {
            0 -> prayName = getString(R.string.fajr)
            1 -> prayName = getString(R.string.shruq)
            2 -> prayName = getString(R.string.zohr)
            3 -> prayName = getString(R.string.assr)
            4 -> prayName = getString(R.string.maaghreb)
            5 -> prayName = getString(R.string.asha)
        }
        binding.prayName.text = prayName
        binding.closeBtn.setOnClickListener { v: View? ->
            val intent = Intent(this@WakeAlarmActivity, PlayerService::class.java)
            intent.putExtra("cancel", true)
            ContextCompat.startForegroundService(this@WakeAlarmActivity, intent)
            finish()
        }
        close.observe(this) { aBoolean: Boolean -> if (aBoolean) finish() }
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onResume() {
        super.onResume()
        val decorView = window.decorView
        val uiOptions: Int
        uiOptions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }

    companion object {
        @JvmField
        var close = MutableLiveData<Boolean>()
    }
}