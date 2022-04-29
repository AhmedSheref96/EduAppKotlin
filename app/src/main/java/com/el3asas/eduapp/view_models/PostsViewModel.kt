package com.el3asas.eduapp.view_models

import com.azan.AzanTimes.fajr
import com.azan.AzanTimes.shuruq
import com.azan.AzanTimes.thuhr
import com.azan.AzanTimes.assr
import com.azan.AzanTimes.maghrib
import com.azan.AzanTimes.ishaa
import com.azan.Time.hour
import com.azan.Time.minute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.os.Handler
import android.util.Log
import io.reactivex.rxjava3.disposables.Disposable
import com.el3asas.eduapp.view_models.PostsViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import android.widget.Toast
import com.el3asas.eduapp.ui.PostsFragment
import com.azan.AzanTimes
import com.azan.Time
import com.el3asas.eduapp.db.LiveDataListener
import com.el3asas.eduapp.db.Repository
import kotlin.Throws
import com.el3asas.eduapp.helpers.SallahAndDiff
import com.el3asas.eduapp.helpers.PrayProperties
import com.el3asas.eduapp.listeners.Listener
import com.el3asas.eduapp.models.*
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    Listener {
    private var currentDate: String? = null
    private var lifecycleOwner: LifecycleOwner? = null
    @JvmField
    var model1: MutableLiveData<Entity>? = null
    @JvmField
    var model2: MutableLiveData<Entity>? = null
    @JvmField
    var model3: MutableLiveData<Entity>? = null
    private var edit: SharedPreferences.Editor? = null
    private var insQ: Disposable? = null
    private var insH: Disposable? = null
    private var insA: Disposable? = null
    fun init(sharedPreferences: SharedPreferences?, lifecycleOwner2: LifecycleOwner?) {
        disposable = CompositeDisposable()
        lifecycleOwner = lifecycleOwner2
        lucklyPref = sharedPreferences
        edit = lucklyPref!!.edit()
        currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Date())
        model1 = MutableLiveData()
        model2 = MutableLiveData()
        model3 = MutableLiveData()
        if (lucklyPref!!.getString("date", "") == currentDate) {
            offlineGetter()
            return
        }
        val i2 = lucklyPref!!.getInt("num", 0) + 1
        onlineGetter(i2, 1)
        insert()
    }

    private fun onlineGetter(i2: Int, i3: Int) {
        failedListener()
        val doq = repository.getOnlyEntityOnliner("Quraan", "num", i2)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ o: Entity ->
                model1!!.value = o
                edit!!.putInt("num", i2)
                edit!!.putString("date", currentDate)
                edit!!.putInt("nOMIDay", i3)
                edit!!.apply()
            }, { e: Throwable -> Log.d(TAG, "onlineGettertttttttt: $e") }
            ) { Log.d(TAG, "onlineGetter: success") }
        val doh = repository.getOnlyEntityOnliner("hades", "num", i2)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ o: Entity -> model2!!.setValue(o) }) { e: Throwable ->
                Log.d(
                    TAG,
                    "onlineGettertttttttt: $e"
                )
            }
        val doa = repository.getOnlyEntityOnliner("aqtbas", "num", i2)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ o: Entity -> model3!!.setValue(o) }) { e: Throwable ->
                Log.d(
                    TAG,
                    "onlineGettertttttttt: $e"
                )
            }
        disposable!!.add(doa)
        disposable!!.add(doh)
        disposable!!.add(doq)
    }

    private fun failedListener() {
        LiveDataListener.setListener(this)
    }

    private fun offlineGetter() {
        Log.d(TAG, "offlineGetter: ++++++++++++++")
        val i = lucklyPref!!.getInt("num", 0)
        findQ(i)
        findH(i)
        findA(i)
    }

    val new: Unit
        get() {
            val i = lucklyPref!!.getInt("nOMIDay", 0) + 1
            if (i < 3) {
                val i2 = lucklyPref!!.getInt("num", 0) + 1
                onlineGetter(i2, i)
                insert()
            }
        }
    val freeQutes: Boolean
        get() {
            val i = lucklyPref!!.getInt("nOMIDay", 0) + 1
            if (i < 3) return true
            Toast.makeText(
                PostsFragment.binding.root.context,
                "لقد تخطيت الحد الاقصى من اليوميات يمكن مراجعه السابق الان",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

    private fun insert() {
        model1!!.observe(lifecycleOwner!!) { entity: Entity ->
            val q = Qentity(entity.quot, entity.info, entity.comment, entity.num, false)
            insQ = repository.insertQr(q)
                .subscribeOn(Schedulers.io())
                .subscribe({ Log.d(TAG, "insert: success") }) { e: Throwable? ->
                    Log.d(
                        TAG,
                        "insert: failed"
                    )
                }
            disposable!!.add(insQ)
        }
        model2!!.observe(lifecycleOwner!!) { entity: Entity ->
            val q = Hentity(entity.quot, entity.info, entity.comment, entity.num, false)
            insH = repository.insertHr(q)
                .subscribeOn(Schedulers.io())
                .subscribe({ Log.d(TAG, "insert: success") }) { e: Throwable? ->
                    Log.d(
                        TAG,
                        "insert: failed"
                    )
                }
            disposable!!.add(insH)
        }
        model3!!.observe(lifecycleOwner!!) { entity: Entity ->
            val q = Aentity(entity.quot, entity.info, entity.comment, entity.num, false)
            insA = repository.insertAr(q)
                .subscribeOn(Schedulers.io())
                .subscribe({ Log.d(TAG, "insert: success") }) { e: Throwable? ->
                    Log.d(
                        TAG,
                        "insert: failed"
                    )
                }
            disposable!!.add(insA)
        }
    }

    fun findQ(i: Int) {
        val dfq = repository.findQr(i)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ o: Entity -> model1!!.setValue(o) }) { e: Throwable ->
                Log.d(
                    TAG,
                    "findQ: $e"
                )
            }
        disposable!!.add(dfq)
    }

    fun findH(i: Int) {
        val dfh = repository.findHr(i)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ o: Entity -> model2!!.setValue(o) }) { e: Throwable ->
                Log.d(
                    TAG,
                    "findQ: $e"
                )
            }
        disposable!!.add(dfh)
    }

    fun findA(i: Int) {
        val dfa = repository.findAr(i)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ o: Entity -> model3!!.setValue(o) }
            ) { e: Throwable -> Log.d(TAG, "findQ: $e") }
        disposable!!.add(dfa)
    }

    fun update(pos: Int, status: Boolean) {
        when (pos) {
            0 -> {
                updateQ = repository.updateQ(lucklyPref!!.getInt("num", 0), status)
                    .subscribeOn(Schedulers.computation())
                    .subscribe({ disposable!!.remove(updateQ) }) { e: Throwable ->
                        Log.d(
                            TAG,
                            "updateQ: aaaaaaeeeeeee$e"
                        )
                    }
                disposable!!.add(updateQ)
            }
            1 -> {
                updateH = repository.updateH(lucklyPref!!.getInt("num", 0), status)
                    .subscribeOn(Schedulers.computation())
                    .subscribe({ disposable!!.remove(updateH) }) { e: Throwable ->
                        Log.d(
                            TAG,
                            "updateH: aaaaaaeeeeeee$e"
                        )
                    }
                disposable!!.add(updateH)
            }
            2 -> {
                updateA = repository.updateA(lucklyPref!!.getInt("num", 0), status)
                    .subscribeOn(Schedulers.computation())
                    .subscribe({ disposable!!.remove(updateA) }) { e: Throwable ->
                        Log.d(
                            TAG,
                            "updateA: aaaaaaeeeeeee$e"
                        )
                    }
                disposable!!.add(updateA)
            }
        }
    }

    fun initPray(p: AzanTimes) {
        if (Companion.prayItems.size == 0) {
            Companion.prayItems.add(
                PrayItem(
                    "صلاه الفجر", getTimeCalender(p.fajr())
                )
            )
            Companion.prayItems.add(
                PrayItem(
                    "صلاه الشروق", getTimeCalender(p.shuruq())
                )
            )
            Companion.prayItems.add(
                PrayItem(
                    "صلاه الظهر", getTimeCalender(p.thuhr())
                )
            )
            Companion.prayItems.add(
                PrayItem(
                    "صلاه العصر", getTimeCalender(p.assr())
                )
            )
            Companion.prayItems.add(
                PrayItem(
                    "صلاه المغرب",
                    getTimeCalender(p.maghrib())
                )
            )
            Companion.prayItems.add(
                PrayItem(
                    "صلاه العشاء", getTimeCalender(p.ishaa())
                )
            )
        }
    }

    private fun getTimeCalender(t: Time): Calendar {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = t.hour
        calendar[Calendar.MINUTE] = t.minute
        return calendar
    }

    val prayItems: List<PrayItem>
        get() = Companion.prayItems

    /** */
    @Throws(ParseException::class)
    fun getSallahLoc(azanTimes: AzanTimes?): SallahAndDiff {
        return PrayProperties.inctance.getSallahLoc(azanTimes)
    }

    /** */
    override fun onCleared() {
        super.onCleared()
        disposable!!.clear()
    }

    override fun onChange(b: Boolean) {
        val handler = Handler()
        val runnable = Runnable { offlineGetter() }
        handler.postDelayed(runnable, 5000)
        if (!b) {
            handler.removeCallbacks(runnable)
        }
    }

    fun setAzkarToDb(entities: List<AzkarEntity?>?) {
        disposable!!.add(
            repository.insertAzkars(entities).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    { Log.d(TAG, "setAzkarToDb: -----------ttttt") }
                ) { throwable: Throwable ->
                    Log.d(
                        TAG,
                        "setAzkarToDb: ------" + throwable.message
                    )
                })
    }

    companion object {
        private const val TAG = "tttttt luckly"
        private var lucklyPref: SharedPreferences? = null
        private var disposable: CompositeDisposable? = null
        private val prayItems: MutableList<PrayItem> = ArrayList()
        private var updateQ: Disposable? = null
        private var updateH: Disposable? = null
        private var updateA: Disposable? = null
    }
}