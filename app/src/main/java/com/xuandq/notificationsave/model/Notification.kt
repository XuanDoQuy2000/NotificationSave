package com.xuandq.notificationsave.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize


@Entity(primaryKeys = arrayOf("message","postedTime"))
@Parcelize
data class Notification (
    val id : Int,
    val key : String,
    val packageName : String,
    val title : String = "",
    val actor : String = "",
    val message : String = "",
    val category : String? = null,
    val postedTime : Long = 0,
    val channelId : String? = null,
    var iconPath : String = "",
    var largeImagePath : String = "",
    var isReaded : Boolean = false
) : Parcelable {

}