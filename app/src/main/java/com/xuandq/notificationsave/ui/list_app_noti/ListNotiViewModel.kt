package com.xuandq.notificationsave.ui.list_app_noti

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xuandq.core.BaseViewModel
import com.xuandq.litekoin.core.inject
import com.xuandq.notificationsave.data.NotyRepository
import com.xuandq.notificationsave.model.AppWithNoti
import com.xuandq.notificationsave.model.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListNotiViewModel() : BaseViewModel() {
    private val dataManager : NotyRepository by inject()
    val listAppWithNoti = MutableLiveData<List<AppWithNoti>>()
    var listNoties : LiveData<List<Notification>> = MutableLiveData()

    fun initData(){
        fetchListNoties()
    }

    fun fetchListAppWithNoti(){
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO){
                dataManager.getAppWithLastNoties()
            }
            Log.d("nnn", "initData: ${dataManager}")
            list?.let {
                listAppWithNoti.value = it
            }
        }
    }

    fun fetchListNoties() {
        listNoties = dataManager.fetchAllNoties()
    }

}