package com.xuandq.notificationsave.di

import android.content.Context
import com.xuandq.litekoin.core.Module
import com.xuandq.litekoin.core.module
import com.xuandq.litekoin.core.startLiteKoin

object NotyContainer {
    @JvmStatic
    fun init(context: Context) {
        startLiteKoin {
            val list = mutableListOf<Module>()
            list.add(module {
                single { context.applicationContext }
            })
            list.addAll(appModules)
            modules(list)
        }
    }
}