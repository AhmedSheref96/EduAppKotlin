package com.el3asas.eduapp.helpers

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
import android.app.AlarmManager
import com.el3asas.eduapp.background.AlertReceiver
import android.app.AlarmManager.AlarmClockInfo
import android.content.ContextWrapper
import android.app.NotificationManager
import android.annotation.TargetApi
import android.os.Build
import android.app.NotificationChannel
import android.content.res.Resources
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.el3asas.eduapp.db.AzkarDB
import androidx.annotation.Keep
import androidx.room.PrimaryKey
import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator

object Image {
    fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight
                && halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun decodeSampledBitmapFromResource(
        res: Resources?, resId: Int,
        reqWidth: Int, reqHeight: Int
    ): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }
}