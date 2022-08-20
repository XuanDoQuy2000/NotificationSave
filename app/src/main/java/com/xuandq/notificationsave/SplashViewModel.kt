package com.xuandq.notificationsave

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.xuandq.core.BaseViewModel
import com.xuandq.notificationsave.data.NotyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class SplashViewModel(private val dataManager : NotyRepository) : BaseViewModel() {
    fun initData(context: Context, onComplete : () -> Unit) = viewModelScope.launch {
        val time = measureTimeMillis {
            if (dataManager.isFirstLaunch()) {
                val listApp = dataManager.getInstalledApps(context)
                withContext(Dispatchers.IO) {
                    dataManager.insertApps(listApp)
                }
                dataManager.setFirstLaunch(true)
            }
        }
        if (time < 5000L) {
            delay(5000 - time)
        }
        onComplete.invoke()
    }
}