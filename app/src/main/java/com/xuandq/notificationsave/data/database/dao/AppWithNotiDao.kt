package com.xuandq.notificationsave.data.database.dao

import androidx.room.Dao
import androidx.room.Transaction
import com.xuandq.notificationsave.data.database.AppDatabase
import com.xuandq.notificationsave.model.AppWithNoti
import com.xuandq.notificationsave.shared.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Dao
abstract class AppWithNotiDao(val database: AppDatabase) {
    private val appDao = database.appDao()
    private val notiDao = database.notiDao()

    @Transaction
    open suspend fun getAppWithLastNoties(): List<AppWithNoti>? {
        val result = ArrayList<AppWithNoti>()
        val listApp = withContext(Dispatchers.IO) { appDao.getAllApp() }
        listApp ?: return null
        listApp.forEach {
            val listLastNoti = withContext(Dispatchers.IO) {
                notiDao.getLastNotiOfApp(
                    it.packageName,
                    Constants.LIMITED_LAST_NOTI
                )
            }
            result.add(AppWithNoti(it, listLastNoti ?: emptyList()))
        }
        return result
    }
}