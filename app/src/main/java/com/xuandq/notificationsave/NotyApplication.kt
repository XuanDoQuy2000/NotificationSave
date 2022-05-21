package com.xuandq.notificationsave

import android.app.Application
import android.util.Log
import com.xuandq.notificationsave.di.NotyContainer

class NotyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("nnn", "onCreate: ")
        NotyContainer.init(applicationContext)
    }
}