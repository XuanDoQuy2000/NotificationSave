package com.xuandq.notificationsave.data.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.xuandq.notificationsave.model.Title

interface TitleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTitle(title: Title)
}