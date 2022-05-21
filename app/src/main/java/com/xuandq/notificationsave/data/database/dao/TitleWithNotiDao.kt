package com.xuandq.notificationsave.data.database.dao

import androidx.room.Dao
import androidx.room.Transaction
import com.xuandq.notificationsave.data.database.AppDatabase
import com.xuandq.notificationsave.model.TitleWithNoti
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Dao
abstract class TitleWithNotiDao(val db: AppDatabase) {
    private val titleDao = db.titleDao()
    private val notiDao = db.notiDao()

    @Transaction
    open suspend fun getListTitleWithNoti(packageName: String): List<TitleWithNoti> =
        coroutineScope {
            val result = ArrayList<TitleWithNoti>()
            val listTitle = withContext(Dispatchers.IO) {
                titleDao.getTitlesOfApp(packageName)
            }
            listTitle?.forEach {
                launch {
                    val lastNoti = withContext(Dispatchers.IO) {
                        notiDao.getNotiByTitle(it.packageName, it.title)
                    }
                    lastNoti?.let { noties ->
                        result.add(TitleWithNoti(it, noties))
                    }
                }
            }
            return@coroutineScope result
        }
}