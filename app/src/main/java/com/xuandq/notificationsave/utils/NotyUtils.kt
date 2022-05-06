package com.xuandq.notificationsave.utils

import android.content.Context
import androidx.core.app.NotificationManagerCompat

object NotyUtils {

    const val ACTION_NOTIFICATION_LISTENER_SETTINGS =
        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

    fun getTitle(s: String?): String? {
        if (s == null) return null
        val index = if (s.indexOf(':') != -1) s.indexOf(':') else s.length
        return s.substring(0, index).trim()
    }

    fun getDescription(s: String?): String? {
        if (s == null) return null
        val index = if (s.indexOf(':') != -1) s.indexOf(':') + 1 else s.length
        return s.substring(index).trim()
    }

    fun isNotificationServiceEnabled(context: Context): Boolean =
        NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)
}