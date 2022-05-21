package com.xuandq.notificationsave.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Embedded
import androidx.room.Relation
import com.xuandq.notificationsave.utils.DateUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppWithNoti(
    @Embedded val app: App,
    @Relation(
        parentColumn = "packageName",
        entityColumn = "packageName"
    )
    val listLastNoties : List<Notification>? = null,
) : Parcelable{
//    fun toText() : String{
//        var res = ""
//        res = res + app.label +"    "+ app.packageName + "\n\n"
//        if (listTitleWithNoties.isNullOrEmpty()) return res
//        for (item in listTitleWithNoties){
//            res = res + item.title.title + "\n"
//            for (noti in item.listNoties){
//                res = res + "    " + noti.message + "  -  " + DateUtils.formatToDate(noti.postedTime) +" "+ DateUtils.formatToTime(noti.postedTime) + "\n"
//            }
//            res = res + "\n"
//        }
//        return res
//    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AppWithNoti>(){
            override fun areItemsTheSame(oldItem: AppWithNoti, newItem: AppWithNoti): Boolean {
                return oldItem.app.packageName == newItem.app.packageName
            }

            override fun areContentsTheSame(oldItem: AppWithNoti, newItem: AppWithNoti): Boolean {
                return oldItem == newItem
            }

        }
    }
}