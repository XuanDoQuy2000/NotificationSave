package com.xuandq.notificationsave.ui.list_title

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xuandq.core.BaseViewModel
import com.xuandq.litekoin.core.inject
import com.xuandq.notificationsave.data.NotyRepository
import com.xuandq.notificationsave.model.Notification
import com.xuandq.notificationsave.model.TitleWithNoti
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListTitleViewModel() : BaseViewModel() {
    private val dataManager: NotyRepository by inject()

    val listTitle by lazy { MutableLiveData<List<TitleWithNoti>>() }
    var listNoties : LiveData<List<Notification>> = MutableLiveData()

    fun initData(packageName : String) {
        fetchListNoties()
    }

    fun fetchListNoties() {
        listNoties = dataManager.fetchAllNoties()
    }

    fun fetchListTitleWithNoti(packageName: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                dataManager.getListTitleWithNoti(packageName)
            }
            listTitle.value = result
        }
    }
}