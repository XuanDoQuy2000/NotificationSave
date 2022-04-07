package com.xuandq.notificationsave.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat

object DateUtils {
    fun formatTime(timeMillis : Long) : String{
        if (System.currentTimeMillis() / DateUtils.DAY_IN_MILLIS == timeMillis / DateUtils.DAY_IN_MILLIS) {
            return formatToTime(timeMillis)
        }else {
            return formatToDate(timeMillis)
        }
    }

    fun formatToTime(timeMillis: Long) : String{
        if (timeMillis == 0L) return ""
        return SimpleDateFormat("HH:mm").format(timeMillis)
    }

    fun formatToDate(timeMillis: Long) : String{
        if (timeMillis == 0L) return ""
        return SimpleDateFormat("MM/dd/yyyy").format(timeMillis)
    }
}