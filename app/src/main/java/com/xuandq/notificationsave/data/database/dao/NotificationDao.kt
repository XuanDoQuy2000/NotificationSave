package com.xuandq.notificationsave.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.xuandq.notificationsave.model.Notification
import com.xuandq.notificationsave.model.Title
import com.xuandq.notificationsave.model.TitleWithNoti

@Dao
interface NotificationDao : BaseDao<Notification> {
    @Query("SELECT * FROM Notification WHERE content = :message AND timeStamp = :postedTime")
    suspend fun get(message: String, postedTime: Long) : Notification?

    @Query("SELECT * FROM Notification")
    suspend fun getAll() : List<Notification>?

    @Query("SELECT * FROM Notification")
    fun fetchAll() : LiveData<List<Notification>>

    @Query ("SELECT * FROM Notification WHERE `key` = :key ORDER BY timeStamp DESC LIMIT 1")
    suspend fun getLastNotiOfGroup(key : String) : Notification?

    @Query ("SELECT * FROM Notification WHERE packageName = :packageName AND title = :title ORDER BY timeStamp DESC")
    suspend fun getNotiByTitle(packageName: String, title : String) : List<Notification>?

    @Query ("SELECT * FROM Notification WHERE packageName = :packageName AND title = :title ORDER BY timeStamp DESC")
    fun fetchNotiByTitle(packageName: String, title : String) : LiveData<List<Notification>>

    @Query("SELECT * FROM Notification WHERE packageName = :packageName ORDER BY timeStamp DESC LIMIT :limit")
    suspend fun getLastNotiOfApp(packageName : String, limit: Int) : List<Notification>?

    @Query ("SELECT * FROM Notification WHERE title = :title AND packageName = :packageName ORDER BY timeStamp DESC LIMIT 1")
    suspend fun getLastNotiOfTitle(title: String, packageName: String) : Notification?

}