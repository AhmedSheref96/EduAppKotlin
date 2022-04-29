package com.el3asas.eduapp.view_models

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import com.el3asas.eduapp.view_models.PageViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import androidx.lifecycle.MutableLiveData
import com.el3asas.eduapp.db.Repository
import com.el3asas.eduapp.models.Entity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@HiltViewModel
class PageViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val allQ: Unit
        get() {
            disposable.add(
                repository.allQr
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { o: List<Entity> -> qData!!.setValue(o) })
        }
    val allH: Unit
        get() {
            disposable.add(
                repository.allHr
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { o: List<Entity> -> hData!!.setValue(o) })
        }
    val allA: Unit
        get() {
            disposable.add(
                repository.allAr
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { o: List<Entity> -> aData!!.setValue(o) })
        }

    override fun onCleared() {
        super.onCleared()
        qData = null
        aData = null
        hData = null
        disposable.clear()
    }

    companion object {
        @JvmField
        var qData: MutableLiveData<List<Entity>>? = MutableLiveData()
        @JvmField
        var hData: MutableLiveData<List<Entity>>? = MutableLiveData()
        @JvmField
        var aData: MutableLiveData<List<Entity>>? = MutableLiveData()
        private val disposable = CompositeDisposable()
    }
}