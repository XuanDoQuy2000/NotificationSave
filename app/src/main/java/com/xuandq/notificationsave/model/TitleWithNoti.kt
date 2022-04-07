package com.xuandq.notificationsave.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.xuandq.notificationsave.model.Notification
import com.xuandq.notificationsave.model.Title
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TitleWithNoti (
    @Embedded val title : Title,
    @Relation (
        parentColumn = "title",
        entityColumn = "title",
        entity = Notification::class
    )
    val listNoties : List<Notification>
) : Parcelable{

}