package com.el3asas.eduapp.db.daos

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
import android.app.PendingIntent
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
import com.el3asas.eduapp.models.Aentity
import com.el3asas.eduapp.models.Hentity
import com.el3asas.eduapp.models.Qentity
import com.el3asas.eduapp.models.PrayEntity
import com.el3asas.eduapp.models.AzkarEntity
import com.el3asas.eduapp.db.daos.AzkarDao
import com.el3asas.eduapp.db.daos.QDao
import com.el3asas.eduapp.db.daos.HDao
import com.el3asas.eduapp.db.daos.ADao
import com.el3asas.eduapp.db.daos.PrayDao
import com.el3asas.eduapp.db.DataBase
import javax.inject.Inject
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentSnapshot
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
import android.app.AlarmManager
import com.el3asas.eduapp.background.AlertReceiver
import android.app.AlarmManager.AlarmClockInfo
import android.content.ContextWrapper
import android.app.NotificationManager
import android.annotation.TargetApi
import android.os.Build
import android.app.NotificationChannel
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.el3asas.eduapp.db.AzkarDB
import androidx.annotation.Keep
import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import androidx.room.*
import com.el3asas.eduapp.models.Entity
import io.reactivex.rxjava3.core.*

@Dao
interface HDao {
    // with rxJava
    @Insert
    fun insertHr(entity: Hentity?): Completable?

    @get:Query("select * from H")
    val allHr: Observable<List<Entity?>?>?

    @Query("update H set status =:s where num=:num")
    fun updateHStatusr(num: Int, s: Boolean): Completable?

    @Query("select * from H where num = :n")
    fun findHr(n: Int): Single<Entity?>?

    @Query("select * from H where status =:s")
    fun getAllLovedHr(s: Int): Flowable<List<Entity?>?>?
}