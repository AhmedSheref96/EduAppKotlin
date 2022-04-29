package com.el3asas.eduapp.db

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
import androidx.room.Dao
import androidx.room.Update
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
import androidx.room.PrimaryKey
import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import com.el3asas.eduapp.models.*
import io.reactivex.rxjava3.core.*

class Repository @Inject constructor(
    private val qDao: QDao,
    private val hDao: HDao,
    private val aDao: ADao,
    private val azkarDao: AzkarDao
) {
    /// Quote
    val allQr: Observable<List<Entity?>?>?
        get() = qDao.allQr

    fun insertQr(note: Qentity?): Completable? {
        return qDao.insertQr(note)
    }

    fun findQr(i: Int): Single<Entity?>? {
        return qDao.findQr(i)
    }

    fun updateQ(num: Int, s: Boolean): Completable? {
        return qDao.updateQStatusr(num, s)
    }

    /// Hades
    val allHr: Observable<List<Entity?>?>?
        get() = hDao.allHr

    fun insertHr(note: Hentity?): Completable? {
        return hDao.insertHr(note)
    }

    fun findHr(i: Int): Single<Entity?>? {
        return hDao.findHr(i)
    }

    fun updateH(num: Int, s: Boolean): Completable? {
        return hDao.updateHStatusr(num, s)
    }

    /// Aqtbas
    val allAr: Observable<List<Entity?>?>?
        get() = aDao.allAr

    fun insertAr(note: Aentity?): Completable? {
        return aDao.insertQr(note)
    }

    fun findAr(i: Int): Single<Entity?>? {
        return aDao.findAr(i)
    }

    fun updateA(num: Int, s: Boolean): Completable? {
        return aDao.updateAStatusr(num, s)
    }

    fun getOnlyEntityOnliner(
        collectionPath: String?,
        FieldName: String?,
        `val`: Int
    ): Observable<Entity?>? {
        val collectionReference = FirebaseFirestore.getInstance().collection(
            collectionPath!!
        )
        val query = collectionReference.whereEqualTo(FieldName!!, `val`)
        val listener = LiveDataListener()
        return listener.getEntity(query)
    }

    fun getAzkarByCat(cat: String?): Single<List<AzkarEntity?>?>? {
        return azkarDao.getAzkarByCat(cat)
    }

    fun getFavCategAzkar(cat: String?, b: Boolean): Single<List<AzkarEntity?>?>? {
        return azkarDao.getFavCategAzkar(cat, b)
    }

    fun getFavAzkar(b: Boolean): Single<List<AzkarEntity?>?>? {
        return azkarDao.getFavAzkar(b)
    }

    fun updateStatus(b: Boolean, id: Int): Completable? {
        return azkarDao.updateStatus(b, id)
    }

    fun insertAzkars(entities: List<AzkarEntity?>?): Completable? {
        return azkarDao.insertAzkar(entities)
    }
}