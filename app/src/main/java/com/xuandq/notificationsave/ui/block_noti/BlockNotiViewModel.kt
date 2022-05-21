package com.xuandq.notificationsave.ui.block_noti

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xuandq.core.BaseViewModel
import com.xuandq.notificationsave.data.NotyRepository
import com.xuandq.notificationsave.model.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BlockNotiViewModel(private val dataManager: NotyRepository) : BaseViewModel() {
    val listApp = MutableLiveData<List<App>>()

    fun initData() {
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) {
                dataManager.getAllApp()
            }
            list?.let {
                listApp.value = it
            }
        }
    }

    fun updateBlockStatus(app: App) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { dataManager.updateApp(app) }
        }
    }
}