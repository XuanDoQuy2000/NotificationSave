package com.xuandq.notificationsave.model

import android.os.Parcelable
import androidx.room.Entity
import com.bumptech.glide.load.resource.bitmap.VideoDecoder.parcel
import kotlinx.android.parcel.Parcelize


@Entity(primaryKeys = arrayOf("content", "timeStamp"))
@Parcelize
data class Notification(
    val id: Int,
    val key: String,
    val packageName: String,
    val title: String,
    val actor: String,
    val content: String,
    val category: String? = null,
    val timeStamp: Long,
    val channelId: String? = null,
    var iconPath: String? = null,
    var largeImagePath: String? = null,
    var isReaded: Boolean = false
) : Parcelable {
    companion object {
        const val DEFAULT_CONTENT = ""
    }
}