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
import android.content.Context
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
import com.azan.Time
import com.azan.astrologicalCalc.Location
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PrayProperties {
    @Throws(ParseException::class)
    fun getSallahLoc(azanTimes: AzanTimes): SallahAndDiff {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        val currentDateandTime = sdf.format(Date())
        val times = arrayOf(
            azanTimes.fajr(), azanTimes.shuruq(), azanTimes.thuhr(),
            azanTimes.assr(), azanTimes.maghrib(), azanTimes.ishaa()
        )
        var c1: Calendar
        var c2: Calendar
        var d1: Date
        var d2: Date
        var t1: String
        var t2: String
        for (i in 0 until times.size - 1) {
            c1 = Calendar.getInstance()
            c1[Calendar.HOUR_OF_DAY] = times[i].hour
            c1[Calendar.MINUTE] = times[i].minute
            c1[Calendar.SECOND] = times[i].second
            c2 = Calendar.getInstance()
            c2[Calendar.HOUR_OF_DAY] = times[i + 1].hour
            c2[Calendar.MINUTE] = times[i + 1].minute
            c2[Calendar.SECOND] = times[i + 1].second
            d1 = c1.time
            t1 = sdf.format(d1)
            d2 = c2.time
            t2 = sdf.format(d2)
            if (isTimeBetweenTwoTime(t1, t2, currentDateandTime)) {
                return SallahAndDiff(
                    i + 1, getDiff2Time(d2.time, d1.time),
                    getDiff2Time(Calendar.getInstance().time.time, c1.time.time)
                )
            }
        }
        c1 = Calendar.getInstance()
        c1[Calendar.HOUR_OF_DAY] = azanTimes.fajr().hour
        c1[Calendar.MINUTE] = azanTimes.fajr().minute
        c1[Calendar.SECOND] = azanTimes.fajr().second
        c1.add(Calendar.DATE, 1)
        c2 = Calendar.getInstance()
        c2[Calendar.HOUR_OF_DAY] = azanTimes.ishaa().hour
        c2[Calendar.MINUTE] = azanTimes.ishaa().minute
        c2[Calendar.SECOND] = azanTimes.ishaa().second
        var d = getDiff2Time(Calendar.getInstance().time.time, c2.time.time)
        if (d / (60 * 60 * 1000) > 9) {
            d = 24 * 60 * 60 * 1000 - d
        }
        return SallahAndDiff(0, getDiff2Time(c2.time.time, c1.time.time), d)
    }

    /** */
    fun setPrayProperaties(context: Context, preferences: SharedPreferences) {
        Log.d(TAG, "setPrayProperaties: +++++++++++++++")
        val today = SimpleDate(GregorianCalendar())
        val longitude = preferences.getString("longitude", "")!!.toDouble()
        val latitude = preferences.getString("latitude", "")!!.toDouble()
        val loc = Location(latitude, longitude, 2.0, 0)
        val azan = Azan(loc, EGYPT_SURVEY)
        val prayerTimes = azan.getPrayerTimes(today)
        try {
            getNextPrayAlarm(context, preferences, prayerTimes)
        } catch (e: ParseException) {
            Log.d("", "setPrayProp: ++++++++++" + e.message)
        }
    }

    private fun startAlarm(context: Context, c: Calendar, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertReceiver::class.java)
        intent.putExtra("requestCode", requestCode)
        @SuppressLint("UnspecifiedImmutableFlag") val broadcast = PendingIntent.getBroadcast(
            context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager.setAlarmClock(AlarmClockInfo(c.timeInMillis, broadcast), broadcast)
    }

    @Throws(ParseException::class)
    private fun getNextPrayAlarm(
        context: Context,
        preferences: SharedPreferences,
        azanTimes: AzanTimes
    ) {
        val nextPray = getNextPray(azanTimes)
        val prays =
            arrayOf("azanFajr", "azanShuruq", "azanZohr", "azanAssr", "azanMaghrib", "azanIshaa")
        val times = azanTimes.times
        for (i in nextPray..5) {
            if (preferences.getBoolean(prays[i], true)) {
                /*Calendar calendar=Calendar.getInstance();
                calendar.add(Calendar.MINUTE,5);
                startAlarm(context,calendar,i);*/
                startAlarm(context, getTimeCalender(times[i]), i)
                return
            }
        }
        for (i in 0 until nextPray) {
            if (preferences.getBoolean(prays[i], true)) {
                /*Calendar calendar=Calendar.getInstance();
                calendar.add(Calendar.MINUTE,5);
                startAlarm(context,calendar,i);*/
                startAlarm(context, getTimeCalender(times[i]), i)
                return
            }
        }
    }

    private fun getTimeCalender(t: Time): Calendar {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = t.hour
        calendar[Calendar.MINUTE] = t.minute
        calendar[Calendar.SECOND] = 0
        if (calendar.timeInMillis < Calendar.getInstance().timeInMillis) calendar.add(
            Calendar.DATE,
            1
        )
        return calendar
    }

    /** */
    @Throws(ParseException::class)
    private fun getNextPray(azanTimes: AzanTimes): Int {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        val currentDateandTime = sdf.format(Date())
        val times = azanTimes.times
        var c1: Calendar
        var c2: Calendar
        var d1: Date?
        var d2: Date?
        var t1: String
        var t2: String
        for (i in 0 until times.size - 1) {
            c1 = Calendar.getInstance()
            c1[Calendar.HOUR_OF_DAY] = times[i].hour
            c1[Calendar.MINUTE] = times[i].minute
            c1[Calendar.SECOND] = times[i].second
            c2 = Calendar.getInstance()
            c2[Calendar.HOUR_OF_DAY] = times[i + 1].hour
            c2[Calendar.MINUTE] = times[i + 1].minute
            c2[Calendar.SECOND] = times[i + 1].second
            d1 = c1.time
            t1 = sdf.format(d1)
            d2 = c2.time
            t2 = sdf.format(d2)
            if (isTimeBetweenTwoTime(t1, t2, currentDateandTime)) {
                return i + 1
            }
        }
        return 0
    }

    private fun getDiff2Time(t1: Long, t2: Long): Long {
        Log.d("", "getDiff2Time: " + (t1 - t2))
        return if (t1 - t2 > 0) t1 - t2 else -(t1 - t2)
    }

    @Throws(ParseException::class)
    private fun isTimeBetweenTwoTime(
        initialTime: String,
        finalTime: String,
        currentTime: String
    ): Boolean {
        Log.d(
            "",
            "isTimeBetweenTwoTime: ttttttttttttt    $initialTime     $finalTime       $currentTime"
        )
        val reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$"
        if (initialTime.matches(reg) && finalTime.matches(reg) &&
            currentTime.matches(reg)
        ) {
            //Start Time
            //all times are from java.util.Date
            @SuppressLint("SimpleDateFormat") val inTime =
                SimpleDateFormat("HH:mm").parse(initialTime)
            val calendar1 = Calendar.getInstance()
            assert(inTime != null)
            calendar1.time = inTime

            //Current Time
            @SuppressLint("SimpleDateFormat") val checkTime =
                SimpleDateFormat("HH:mm").parse(currentTime)
            val calendar3 = Calendar.getInstance()
            assert(checkTime != null)
            calendar3.time = checkTime

            //End Time
            @SuppressLint("SimpleDateFormat") val finTime =
                SimpleDateFormat("HH:mm").parse(finalTime)
            val calendar2 = Calendar.getInstance()
            assert(finTime != null)
            calendar2.time = finTime
            if (finalTime.compareTo(initialTime) < 0) {
                calendar2.add(Calendar.DATE, 1)
                calendar3.add(Calendar.DATE, 1)
            }
            val actualTime = calendar3.time
            return (actualTime.after(calendar1.time) ||
                    actualTime.compareTo(calendar1.time) == 0) &&
                    actualTime.before(calendar2.time)
        } else {
            Log.d("", "isTimeBetweenTwoTime: nottttttttttttt")
        }
        return false
    }

    companion object {
        private const val TAG = "PrayProperties"
        var prayProperties: PrayProperties? = null
        val inctance: PrayProperties?
            get() {
                if (prayProperties == null) prayProperties = PrayProperties()
                return prayProperties
            }
    }
}