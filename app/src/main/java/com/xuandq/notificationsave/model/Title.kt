package com.xuandq.notificationsave.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(primaryKeys = arrayOf("title", "packageName"))
@Parcelize
data class Title(
    val title: String,
    val actor: String,
    val packageName: String
) : Parcelable {
    companion object {
        const val DEFAULT_TITLE = "Unknown"
        const val DEFAULT_ACTOR = ""
    }
}