package com.xuandq.notificationsave.data.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.xuandq.notificationsave.model.App

interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: App)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllApp(listApp : List<App>)

    @Update
    suspend fun updateApp(app : App)

    @Update
    suspend fun updateAllApp(apps : List<App>)

    @Query("SELECT * FROM App")
    fun getAllApp() : List<App>
}