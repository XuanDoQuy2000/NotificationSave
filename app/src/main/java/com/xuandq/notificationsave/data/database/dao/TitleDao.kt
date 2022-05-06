package com.xuandq.notificationsave.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.xuandq.notificationsave.model.Title

@Dao
interface TitleDao : BaseDao<Title> {
    @Query("SELECT * FROM Title WHERE title = :title AND packageName = :packageName")
    suspend fun get(title: String, packageName: String) : Title?

    @Query("SELECT * FROM Title")
    suspend fun getAll() : List<Title>?
}