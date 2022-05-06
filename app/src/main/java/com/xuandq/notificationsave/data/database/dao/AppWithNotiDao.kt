package com.xuandq.notificationsave.data.database.dao

import androidx.room.Dao
import androidx.room.Transaction
import com.xuandq.notificationsave.data.database.AppDatabase
import com.xuandq.notificationsave.model.AppWithNoti
import com.xuandq.notificationsave.shared.Constants

@Dao
abstract class AppWithNotiDao(val database: AppDatabase) {
    private val appDao = database.appDao()
    private val notiDao = database.notiDao()

    @Transaction
    open suspend fun getAppWithLastNoties() : List<AppWithNoti>? {
        val result = ArrayList<AppWithNoti>()
        val listApp = appDao.getAllApp()
        listApp ?: return null
        listApp.forEach{
            val listLastNoti = notiDao.getLastNotiOfApp(it.packageName, Constants.LIMITED_LAST_NOTI)
            result.add(AppWithNoti(it, listLastNoti ?: emptyList()))
        }
        return result
    }
}