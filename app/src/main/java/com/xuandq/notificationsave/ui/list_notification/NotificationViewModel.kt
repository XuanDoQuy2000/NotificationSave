package com.xuandq.notificationsave.ui.list_notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xuandq.core.BaseViewModel
import com.xuandq.litekoin.core.inject
import com.xuandq.notificationsave.data.NotyRepository
import com.xuandq.notificationsave.model.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationViewModel() : BaseViewModel() {
    private val dataManager : NotyRepository by inject()

    var listNoties : LiveData<List<Notification>> = MutableLiveData()

    fun initData(packageName: String, title: String) {
        listNoties = dataManager.fetchListNotiesOfTitle(packageName, title)
    }
}