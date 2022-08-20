package com.xuandq.notificationsave.services

import android.app.PendingIntent
import android.app.RemoteInput
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import com.xuandq.litekoin.core.inject
import com.xuandq.notificationsave.data.NotyRepository
import com.xuandq.notificationsave.model.App
import com.xuandq.notificationsave.model.Notification
import com.xuandq.notificationsave.model.Title
import com.xuandq.notificationsave.utils.NotyUtils


class NotificationService : NotificationListenerService(), CoroutineScope {

    private val dataManager: NotyRepository by inject()

    override fun onNotificationPosted(statusBarNotification: StatusBarNotification?) {
        statusBarNotification?.let { sbn ->
            launch {
                var app = withContext(Dispatchers.IO) { dataManager.getApp(sbn.packageName) }
                if (app == null) {
                    app = dataManager.creatAppAsync(applicationContext, sbn.packageName)
                    dataManager.insertApp(app)
                }
                saveNotificaiton(sbn, app)
            }
        }
        super.onNotificationPosted(statusBarNotification)
    }

    private suspend fun saveNotificaiton(sbn: StatusBarNotification, app: App) {
        if (app.isSaveNoti) {
            onSaveNotification(sbn)
        }
        if (app.isBlockNoti) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cancelNotification(sbn.key)
            }
        }
    }

    private suspend fun onSaveNotification(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val type = extras.get(android.app.Notification.EXTRA_TEMPLATE).toString()
        Log.d("xuandq", "onSaveNotification: $type")

        when (extras.get(android.app.Notification.EXTRA_TEMPLATE).toString()) {
            "android.app.Notification\$InboxStyle" -> {
                onInboxStyle(sbn)
            }
            "android.app.Notification\$BigTextStyle" -> {
                onBigTextStyle(sbn)
            }
            "android.app.Notification\$MessagingStyle" -> {
                onMessagingStyle(sbn)
            }
            "android.app.Notification\$MediaStyle" -> {
                onMediaStyle(sbn)
            }
            else -> {
                onOtherStyle(sbn)
            }
        }
    }

    private suspend fun onOtherStyle(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val title = extras.get("android.title")?.toString()
        val content = extras.get("android.text")?.toString()
        onSave(sbn, title, content)
    }

    private suspend fun onMessagingStyle(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val messages = extras.get(android.app.Notification.EXTRA_MESSAGES) as Array<Parcelable>?
            messages?.let {
                val title = (extras.get(android.app.Notification.EXTRA_CONVERSATION_TITLE)
                    ?: extras.get("android.title"))?.toString()
                for (tmp in messages) {
                    val m = (tmp as Bundle)
                    val content = m.get("text")?.toString()
                    val time = m.get("time") as Long?
                    val sender = m.get("sender")?.toString()
                    onSave(sbn, title, content, sender, time)
                }
            }
        } else {
            TODO("VERSION.SDK_INT < N")
        }
    }

    private suspend fun onBigTextStyle(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val title = extras.get("android.title")?.toString()
        val content = extras.get(android.app.Notification.EXTRA_BIG_TEXT)?.toString()
        onSave(sbn, title, content)
    }

    private suspend fun onInboxStyle(sbn: StatusBarNotification) {
        if (sbn.notification.flags.and(android.app.Notification.FLAG_GROUP_SUMMARY)
            == android.app.Notification.FLAG_GROUP_SUMMARY
        ) {
            // nếu thông báo là thông báo tổng hợp
            onOtherStyle(sbn)
        } else {
            // nếu thông báo là thông báo tin nhắn
            val extras = sbn.notification.extras
            val messages = extras.getCharSequenceArray(android.app.Notification.EXTRA_TEXT_LINES)
            messages?.let {
                val lastNoti = dataManager.getLastNotiOfGroupByKey(sbn.key)
                val index = messages.indexOfFirst {
                    it.toString() == lastNoti.toString()
                }
                for (i in index + 1 until messages.size - 1) {
                    val title = extras.get("android.title")?.toString()
                    val content = messages[i].toString()
                    onSave(sbn, title, content)
                }
            }
        }
    }

    private suspend fun onMediaStyle(sbn: StatusBarNotification){

    }

    private suspend fun onSave(
        sbn: StatusBarNotification,
        title: String?,
        content: String?,
        actor: String? = null,
        time: Long? = null
    ) {
        Log.d("xuandq", "onSave:   key = ${sbn.key}")
        Log.d("xuandq", "onSave:   id = ${sbn.id}")
        Log.d("xuandq", "onSave:   title = ${title}")
        Log.d("xuandq", "onSave:   content = ${content}")
        Log.d("xuandq", "onSave:   time = ${sbn.notification.`when`}")
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sbn.notification.channelId
        } else {
            null
        }
        if (title == null && content == null) return
        dataManager.insertTitle(
            Title(
                NotyUtils.getTitle(title) ?: Title.DEFAULT_TITLE,
                NotyUtils.getActor(title),
                sbn.packageName
            )
        )
        dataManager.addNotification(
            Notification(
                sbn.id,
                sbn.key,
                sbn.packageName,
                NotyUtils.getTitle(title) ?: Title.DEFAULT_TITLE,
                actor ?: NotyUtils.getActor(title),
                content ?: Notification.DEFAULT_CONTENT,
                sbn.notification.category,
                timeStamp = time ?: sbn.notification.`when` ?: 0,
                channelId = channelId
            ),
            sbn.notification.largeIcon
        )
        intentMap.put(sbn.key, sbn.notification.contentIntent)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    companion object {
        val intentMap: MutableMap<String, PendingIntent?> = HashMap()
        val remoteInputMap: MutableMap<String, List<RemoteInput>> = HashMap()
        val actionIntentMap: MutableMap<String, PendingIntent?> = HashMap()
    }
}
