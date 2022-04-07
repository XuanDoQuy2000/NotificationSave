package com.xuandq.notificationsave.data.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.xuandq.notificationsave.model.App
import com.xuandq.notificationsave.model.Notification
import com.xuandq.notificationsave.model.Title
import com.xuandq.notificationsave.model.TitleWithNoti

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoti (noti : Notification)

    @Query("SELECT * FROM Notification WHERE packageName = :packageName ORDER BY postedTime DESC LIMIT :limit")
    suspend fun getLastNotiOfApp(packageName : String, limit: Int) : List<Notification>


    @Query("SELECT * FROM Title WHERE packageName = :packageName")
    suspend fun getListTitleOfGroup (packageName: String) : List<Title>

    @Query("SELECT * FROM Notification WHERE packageName = :packageName AND title = :title ORDER BY postedTime DESC")
    suspend fun getNotiByTitle(packageName: String, title : String) : List<Notification>

    @Transaction
    suspend fun getTitleWithNotiesOfApp(packageName: String) : List<TitleWithNoti>{
        val result = ArrayList<TitleWithNoti>()
        val listTitle= getListTitleOfGroup(packageName)
        listTitle.forEach {
            val listNotiOfTitle = getNotiByTitle(packageName,it.title)
            if (listNotiOfTitle.isNotEmpty()) result.add(TitleWithNoti(it,listNotiOfTitle))
        }
        return result
    }

    @Update
    suspend fun readAllNoti(listNoti : List<Notification>)

    @Query("SELECT isBlockNoti FROM App WHERE packageName = :packageName")
    suspend fun isBlockedNoti(packageName: String) : Boolean

    @Query("SELECT isSaveNoti FROM App WHERE packageName = :packageName")
    suspend fun isSaveNoti(packageName: String) : Boolean

}