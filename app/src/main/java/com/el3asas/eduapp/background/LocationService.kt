package com.el3asas.eduapp.background

import android.Manifest
import com.azan.AzanTimes.fajr
import com.azan.AzanTimes.shuruq
import com.azan.AzanTimes.thuhr
import com.azan.AzanTimes.assr
import com.azan.AzanTimes.maghrib
import com.azan.AzanTimes.ishaa
import com.azan.Time.hour
import com.azan.Time.minute
import com.azan.Time.second
import com.azan.Method.Companion.EGYPT_SURVEY
import com.azan.Azan.getPrayerTimes
import com.azan.AzanTimes.times
import android.content.Intent
import com.el3asas.eduapp.background.PrayerBGS
import com.el3asas.eduapp.helpers.NotificationHelper
import com.el3asas.eduapp.ui.MainActivity
import android.annotation.SuppressLint
import android.os.IBinder
import com.el3asas.eduapp.background.PlayerService
import androidx.core.content.ContextCompat
import android.content.SharedPreferences
import com.el3asas.eduapp.helpers.PrayProperties
import android.media.MediaPlayer
import com.el3asas.eduapp.ui.WakeAlarmActivity
import com.el3asas.eduapp.R
import android.media.MediaPlayer.OnCompletionListener
import com.el3asas.eduapp.background.LocationService
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Looper
import androidx.room.Dao
import com.el3asas.eduapp.models.Aentity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import com.el3asas.eduapp.models.Hentity
import com.el3asas.eduapp.models.Qentity
import com.el3asas.eduapp.models.PrayEntity
import androidx.room.Update
import com.el3asas.eduapp.models.AzkarEntity
import androidx.room.Database
import androidx.room.RoomDatabase
import com.el3asas.eduapp.db.daos.AzkarDao
import com.el3asas.eduapp.db.daos.QDao
import com.el3asas.eduapp.db.daos.HDao
import com.el3asas.eduapp.db.daos.ADao
import com.el3asas.eduapp.db.daos.PrayDao
import com.el3asas.eduapp.db.DataBase
import androidx.room.Room
import javax.inject.Inject
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentSnapshot
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.ObservableEmitter
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import android.widget.Toast
import com.el3asas.eduapp.ui.PostsFragment
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import kotlin.Throws
import com.azan.AzanTimes
import com.el3asas.eduapp.helpers.SallahAndDiff
import com.azan.astrologicalCalc.SimpleDate
import com.azan.Azan
import com.el3asas.eduapp.background.AlertReceiver
import android.app.AlarmManager.AlarmClockInfo
import android.content.ContextWrapper
import android.annotation.TargetApi
import android.app.*
import android.os.Build
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.el3asas.eduapp.db.AzkarDB
import androidx.annotation.Keep
import androidx.room.PrimaryKey
import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import android.util.Log
import com.google.android.gms.location.*

class LocationService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        isRunning = true
        startForeground(10, generateNotfication())
        Log.d(TAG, "onCreate: ++++++++++ssssssss+++++++++")
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest.create()
        val callback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Log.d(
                    TAG,
                    "onLocationResult: +++++++++++++++++++++++++++++++" + locationResult.lastLocation.toString()
                )
                MainActivity.longitude.value = locationResult.lastLocation.longitude
                MainActivity.latitude.value = locationResult.lastLocation.latitude
                stopSelf()
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                callback,
                Looper.getMainLooper()
            )
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun generateNotfication(): Notification {
        val helper = NotificationHelper(this)
        helper.setNotification(
            NOTIFICATION_CHANNEL_ID,
            NOTI_NAME,
            "?????????? ?????????? ????????????",
            "???????? ???????????? ???? ???????????? ???????????? ???????? ???????????? ???????????? ???????????? ???????? ????????"
        )
        return helper.channelNotification.build()
    }

    companion object {
        private const val NOTI_NAME = "LocationForgroundService"
        private const val NOTIFICATION_CHANNEL_ID = "LocationService"
        private const val TAG = "LocationService"
        var isRunning = false
    }
}