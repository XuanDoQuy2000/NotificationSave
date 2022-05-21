package com.xuandq.notificationsave.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xuandq.notificationsave.data.database.dao.*
import com.xuandq.notificationsave.model.App
import com.xuandq.notificationsave.model.Notification
import com.xuandq.notificationsave.model.Title

@Database(entities = arrayOf(
    App::class,
    Notification::class,
    Title::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao() : AppDao
    abstract fun notiDao() : NotificationDao
    abstract fun titleDao() : TitleDao
    abstract fun appWithNotiDao() : AppWithNotiDao
    abstract fun titleWithNotiDao() : TitleWithNotiDao
}