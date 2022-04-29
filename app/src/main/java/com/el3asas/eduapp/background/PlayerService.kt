package com.el3asas.eduapp.background

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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
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
import java.util.*

class PlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.getBooleanExtra("cancel", false) && mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            stopSelf()
        } else {
            val notificationHelper = NotificationHelper(this)
            val i = intent.getIntExtra("requestCode", 0)
            when (i) {
                0 -> notificationHelper.setNotification(
                    channelId,
                    channelName,
                    "صلاه الفجر",
                    "حان الان موعد اذان صلاه الفجر"
                )
                1 -> notificationHelper.setNotification(
                    channelId,
                    channelName,
                    "صلاه الشروق",
                    "حان الان موعد صلاه الشروق"
                )
                2 -> {
                    val calendar = Calendar.getInstance()
                    if (calendar[Calendar.DAY_OF_WEEK] == Calendar.FRIDAY) notificationHelper.setNotification(
                        channelId, channelName, "صلاه الجمعه", "حان الان موعد اذان صلاه الجمعه"
                    ) else notificationHelper.setNotification(
                        channelId, channelName, "صلاه الظهر", "حان الان موعد اذان صلاه الظهر"
                    )
                }
                3 -> notificationHelper.setNotification(
                    channelId,
                    channelName,
                    "صلاه العصر",
                    "حان الان موعد اذان صلاه العصر"
                )
                4 -> notificationHelper.setNotification(
                    channelId,
                    channelName,
                    "صلاه المغرب",
                    "حان الان موعد اذان صلاه المغرب"
                )
                5 -> notificationHelper.setNotification(
                    channelId,
                    channelName,
                    "صلاه العشاء",
                    "حان الان موعد اذان صلاه العشاء"
                )
            }
            val intent1 = Intent(this, WakeAlarmActivity::class.java)
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent1.putExtra("requestCode", i)
            val pendingIntent =
                PendingIntent.getActivity(this, i, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
            val cancelIntent = Intent(this, PlayerService::class.java)
            val cancelMediaPlayer = PendingIntent.getService(
                this,
                i,
                cancelIntent.putExtra("cancel", true),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            mediaPlayer = if (i == 0) MediaPlayer.create(this, R.raw.afasy) else MediaPlayer.create(
                this,
                R.raw.afasyd
            )
            mediaPlayer.start()
            mediaPlayer.setLooping(false)
            mediaPlayer.setVolume(.7f, .7f)
            mediaPlayer.setOnCompletionListener(OnCompletionListener { mp: MediaPlayer? ->
                if (WakeAlarmActivity.close != null) {
                    WakeAlarmActivity.close.setValue(true)
                    stopSelf()
                }
            })
            startForeground(
                1, notificationHelper.channelNotification
                    .setFullScreenIntent(pendingIntent, true)
                    .addAction(
                        R.drawable.ic_app_ic,
                        getString(R.string.cancel_media_player),
                        cancelMediaPlayer
                    )
                    .build()
            )
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer = null
    }

    companion object {
        private const val channelId = "forGroundNoti"
        private const val channelName = "PlayerChannel"
    }
}