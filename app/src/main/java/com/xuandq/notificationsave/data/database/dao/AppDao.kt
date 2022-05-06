package com.xuandq.notificationsave.data.database.dao

import androidx.room.*
import com.xuandq.notificationsave.model.App

@Dao
interface AppDao : BaseDao<App> {

    @Query("SELECT * FROM APP WHERE packageName = :packageName")
    suspend fun getApp(packageName: String): App?

    @Query("SELECT * FROM App")
    suspend fun getAllApp(): List<App>?
}