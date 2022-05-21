package com.xuandq.notificationsave.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import com.bumptech.glide.load.resource.bitmap.VideoDecoder.parcel
import com.xuandq.core.isNotNullOrBlank
import com.xuandq.core.isNotNullOrEmpty
import com.xuandq.notificationsave.utils.DateUtils
import com.xuandq.notificationsave.utils.NotyUtils
import kotlinx.android.parcel.Parcelize


@Entity(primaryKeys = arrayOf("content", "timeStamp"))
@Parcelize
data class Notification(
    val id: Int,
    val key: String,
    val packageName: String,
    val title: String,
    val actor: String?,
    val content: String,
    val category: String? = null,
    val timeStamp: Long,
    val channelId: String? = null,
    var iconPath: String? = null,
    var largeImagePath: String? = null,
    var isReaded: Boolean = false
) : Parcelable {

    fun getDescription() = if (actor.isNotNullOrBlank()){
        "$actor : $content"
    } else {
        content
    }

    fun getDateTimeFormat() = DateUtils.formatTime(timeStamp)

    fun getTimeFormat() = DateUtils.formatToTime(timeStamp)

    fun getDateFormat() = DateUtils.formatToDate(timeStamp)

    companion object {
        const val DEFAULT_CONTENT = ""
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Notification>(){
            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem.timeStamp == newItem.timeStamp && oldItem.content == newItem.content
            }

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem == newItem
            }

        }
    }

}