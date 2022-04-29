package com.el3asas.eduapp.ui

import android.Manifest
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import android.os.Bundle
import com.el3asas.eduapp.R
import com.google.android.gms.location.LocationServices
import com.el3asas.eduapp.ui.MainActivity
import android.annotation.SuppressLint
import com.google.android.gms.tasks.OnSuccessListener
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import com.el3asas.eduapp.background.LocationService
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.location.Location
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var GpsOpend = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        preferences = getSharedPreferences("Luckly", 0)
        if (preferences.getBoolean("locationSaved", false)) {
            longitude.setValue(
                preferences.getString("longitude", "")!!.toDouble()
            )
            latitude.setValue(
                preferences.getString("latitude", "")!!.toDouble()
            )
        } else {
            location
        }
    }

    @SuppressLint("MissingPermission")
    private fun showLocation() {
        if (isGpsOn) {
            mFusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.d(ContentValues.TAG, "showLocation: ++++++++++++")
                        latitude.value = location.latitude
                        longitude.value = location.longitude
                        GpsOpend = false
                    } else {
                        Log.d(ContentValues.TAG, "showLocation: ---------------")
                        showLocation()
                    }
                }
        } else {
            showGpsAlertDialog()
        }
    }

    private fun showGpsAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(
            this
        )
        alertDialogBuilder
            .setMessage("فتح ال GPS لتحديث بيانات صلواتك ؟")
            .setCancelable(false)
            .setPositiveButton(
                "فتح"
            ) { dialog: DialogInterface, id: Int ->
                val callGPSSettingIntent = Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
                startActivity(callGPSSettingIntent)
                GpsOpend = true
                dialog.dismiss()
            }
        val alert = alertDialogBuilder.create()
        alert.show()
    }

    private val isGpsOn: Boolean
        private get() {
            val manager = getSystemService(LOCATION_SERVICE) as LocationManager
            return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            location
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private val location: Unit
        private get() {
            if (Build.VERSION.SDK_INT <= 28) {
                if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                ) {
                    val permissions = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    requestPermissions(permissions, 44)
                } else showLocation()
            } else {
                if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                ) {
                    val permissions = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    requestPermissions(permissions, 44)
                } else {
                    val locationService = Intent(this, LocationService::class.java)
                    ContextCompat.startForegroundService(this, locationService)
                }
            }
        }

    public override fun onResume() {
        super.onResume()
        if (GpsOpend) {
            showLocation()
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    }

    companion object {
        @JvmField
        val latitude = MutableLiveData<Double>()
        @JvmField
        val longitude = MutableLiveData<Double>()
        protected lateinit var preferences: SharedPreferences0
        private const val LOCATION_REQUEST_CODE = 44
    }
}