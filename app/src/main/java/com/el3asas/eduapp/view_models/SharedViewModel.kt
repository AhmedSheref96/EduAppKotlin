package com.el3asas.eduapp.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class SharedViewModel : ViewModel() {
    var searchQueryLiveData = MutableLiveData<String>()
    fun setData(str: String) {
        searchQueryLiveData.value = str
    }

    public override fun onCleared() {
        super.onCleared()
    }
}