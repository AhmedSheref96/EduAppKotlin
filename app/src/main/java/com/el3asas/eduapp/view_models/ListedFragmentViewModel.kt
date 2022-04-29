package com.el3asas.eduapp.view_models

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.el3asas.eduapp.db.Repository
import com.el3asas.eduapp.models.AzkarEntity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.ArrayList

@HiltViewModel
class ListedFragmentViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    private val liveData = MutableLiveData<List<AzkarEntity>>()
    private val disposable = CompositeDisposable()
    private var list: MutableList<AzkarEntity>? = null
    fun getAllAzkar(cat: Array<String?>): MutableLiveData<List<AzkarEntity>> {
        list = ArrayList()
        for (c in cat) {
            disposable.add(
                repository.getAzkarByCat(c).observeOn(AndroidSchedulers.mainThread()).subscribeOn(
                    Schedulers.computation()
                )
                    .subscribe({ azkarEntities: List<AzkarEntity>? ->
                        (list as ArrayList<AzkarEntity>).addAll(azkarEntities!!)
                        liveData.setValue(list!!)
                        Log.d("TAG", "getAllAzkar: --------------")
                    }
                    ) { throwable: Throwable ->
                        Log.d(
                            "TAG",
                            "getAllAzkar: ------" + throwable.message
                        )
                    })
        }
        return liveData
    }

    fun getAllAzkar(cat: Array<String?>, status: Boolean): MutableLiveData<List<AzkarEntity>> {
        list = ArrayList()
        for (c in cat) {
            disposable.add(
                repository.getFavCategAzkar(c, status).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(
                        Schedulers.computation()
                    )
                    .subscribe({ azkarEntities: List<AzkarEntity>? ->
                        (list as ArrayList<AzkarEntity>).addAll(azkarEntities!!)
                        liveData.setValue(list!!)
                    }
                    ) { throwable: Throwable ->
                        Log.d(
                            "TAG",
                            "getAllAzkar: ------" + throwable.message
                        )
                    })
        }
        return liveData
    }

    fun getFavAzkar(b: Boolean): MutableLiveData<List<AzkarEntity>> {
        list = ArrayList()
        disposable.add(
            repository.getFavAzkar(b).observeOn(AndroidSchedulers.mainThread()).subscribeOn(
                Schedulers.computation()
            )
                .subscribe({ azkarEntities: List<AzkarEntity> ->
                    (list as ArrayList<AzkarEntity>).addAll(azkarEntities)
                    liveData.setValue(list!!)
                    Log.d("TAG", "getAllAzkar: --------------" + azkarEntities[0].zekr)
                }
                ) { throwable: Throwable ->
                    Log.d(
                        "TAG",
                        "getAllAzkar: ------" + throwable.message
                    )
                })
        return liveData
    }

    fun updateStatus(status: Boolean, id: Int) {
        disposable.add(
            repository.updateStatus(status, id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    { Log.d("", "updateStatus: --") }
                ) { throwable: Throwable? -> Log.d("", "updateStatus: error----") })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        list = null
    }
}