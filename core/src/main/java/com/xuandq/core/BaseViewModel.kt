package com.xuandq.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Exception

open class BaseViewModel : ViewModel() {
    protected val _commonError = MutableLiveData<Exception>()
    val commonError : LiveData<Exception> = _commonError

    protected val _loading= MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

}