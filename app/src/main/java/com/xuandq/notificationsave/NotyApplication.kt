package com.xuandq.notificationsave

import android.app.Application
import com.xuandq.notificationsave.di.NotyContainer

class NotyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NotyContainer.init(applicationContext)
    }
}