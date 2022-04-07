package com.xuandq.notificationsave.services

import android.app.PendingIntent
import android.app.RemoteInput
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.xuandq.notificationios.data.AppDataBaseImp
import com.xuandq.notificationios.data.daos.NotificationDao
import com.xuandq.notificationios.data.daos.TitleDao
import com.xuandq.notificationios.data.model.Notification
import com.xuandq.notificationios.data.model.Title
import com.xuandq.notificationios.utils.XTools
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import android.graphics.Bitmap
import com.xuandq.notificationios.data.model.App


class NotificationService : NotificationListenerService(), CoroutineScope {


    companion object {
        val intentMap: MutableMap<String, PendingIntent?> = HashMap()
        val remoteInputMap : MutableMap<String, List<RemoteInput>> = HashMap()
        val actionIntentMap : MutableMap<String, PendingIntent?> = HashMap()
    }


    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val notiDao = AppDataBaseImp.getNoticationDao(this)
        val titleDao = AppDataBaseImp.getTitleDao(this)
        val appDao = AppDataBaseImp.getAppDao(this)


        sbn?.let {
            launch {

                val isBlockedNoti = async(Dispatchers.IO) { appDao.isBlockedNoti(sbn.packageName) }
                val isSaveNoti = async(Dispatchers.IO) { appDao.isSaveNoti(sbn.packageName) }

                if (isBlockedNoti.await() != null && isSaveNoti.await() != null) {
                    blockSaveNoti(isBlockedNoti.await(), sbn, isSaveNoti.await(), notiDao, titleDao)
                } else {
                    launch {
                        val info = packageManager.getApplicationInfo(sbn.packageName, 0)
                        val label = info.loadLabel(packageManager).toString()
                        val icon = info.loadIcon(packageManager)
                        val iconPath = XTools.saveIcon(
                                applicationContext,
                                XTools.drawableToBitmap(icon),
                                1,
                                sbn.packageName
                            )

                        val app = App(sbn.packageName, iconPath, label)
                        appDao.insertApp(app)
                    }
                    blockSaveNoti(false, sbn, true, notiDao, titleDao)
                }
            }
        }

        val extras = sbn!!.notification.extras

        Log.d(
            "ssss",
            "onNotificationPosted: style : " + extras.get(android.app.Notification.EXTRA_TEMPLATE)
        )

        Log.d(
            "sss",
            "onNotificationPosted: title : " + sbn!!.notification.extras.get("android.title")
        )
        Log.d(
            "sss",
            "onNotificationPosted: content : " + sbn!!.notification.extras.get("android.text")
        )

        Log.d("sss", "onNotificationPosted: key : " + sbn?.key)
        Log.d("sss", "onNotificationPosted: posted time : " + sbn?.postTime)
        Log.d("sss", "onNotificationPosted: package name :  " + sbn?.packageName)
        Log.d("sss", "onNotificationPosted: id : " + sbn?.id)
        Log.d(
            "sss",
            "onNotificationPosted: noti.category : " + sbn?.notification?.extras?.get(android.app.Notification.EXTRA_TEMPLATE)
        )
        Log.d("sss", "onNotificationPosted: noti.category : " + sbn?.notification.contentIntent)
        Log.d("sss", "onNotificationPosted: when : " + sbn?.notification?.`when`)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("sss", "onNotificationPosted: channel id : " + sbn?.notification?.channelId)
        }

        if (sbn.packageName.contains("tele")) {
            Log.d("zalooo", "onNotificationPosted: 1")
            val extras: Bundle = sbn.getNotification().extras
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val b =
                    extras.get(android.app.Notification.EXTRA_MESSAGES) as Array<Parcelable>?
                if (b != null) {
                    var content = ""
                    for (tmp in b) {
                        val msgBundle = tmp as Bundle
                        content = content.toString() + msgBundle.getString("text") + "\n"

                        val key = msgBundle.keySet()
                        for (k in key) {
                            Log.d("zalooo", "onNotificationPosted: " + k)
                        }
                    }
                    Log.d("zalooo", "onNotificationPosted: " + content)
                } else {
                    Log.d("zalooo", "onNotificationPosted: 2")
                }
            }
        }
    }

    private suspend fun blockSaveNoti(
        isBlockedNoti: Boolean,
        sbn: StatusBarNotification,
        isSaveNoti: Boolean,
        notiDao: NotificationDao,
        titleDao: TitleDao
    ) = withContext(Dispatchers.Main) {
        if (isSaveNoti) {
            onSave(sbn, notiDao, titleDao)
        }
        if (isBlockedNoti) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cancelNotification(sbn.key)
            }
        }
    }


    private suspend fun onSave(
        sbn: StatusBarNotification?,
        notiDao: NotificationDao,
        titleDao: TitleDao
    ) {
        sbn?.let {

            val extras = sbn!!.notification.extras

            var title = if (extras.get("android.title") == null) "" else
                extras.get("android.title").toString().trim()

            var content = ""

            val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sbn.notification.channelId
            } else {
                null
            }

            when (extras.get(android.app.Notification.EXTRA_TEMPLATE).toString()) {
                "android.app.Notification\$InboxStyle" -> {

                    if (sbn.notification.flags.and(android.app.Notification.FLAG_GROUP_SUMMARY) == android.app.Notification.FLAG_GROUP_SUMMARY){
                        Log.d("inboxxx", "onSave: is summary group")
                        content = if (extras.get("android.text") == null) "" else
                            extras.get("android.text").toString().trim()

                        if (title == "" && content == "") return

                        val mTitle = Title(
                            XTools.getTitle(title),
                            XTools.getDescription(title),
                            sbn.packageName
                        )
                        saveTitle(titleDao, mTitle)

                        val notification = Notification(
                            sbn.id,
                            sbn.key,
                            sbn.packageName,
                            XTools.getTitle(title),
                            XTools.getDescription(title),
                            content,
                            sbn.notification.category,
                            postedTime = sbn.notification.`when`,
                            channelId = channelId
                        )
                        saveNotification(notiDao, notification, sbn.notification.largeIcon)
                        return
                    }


                    if (extras.containsKey(android.app.Notification.EXTRA_TEXT_LINES)) {

                        val lastNoti = withContext(Dispatchers.IO) {
                            notiDao.getLastNotiOfGroup(sbn.key)
                        }

                        val chats =
                            extras.getCharSequenceArray(android.app.Notification.EXTRA_TEXT_LINES)
                        if (chats != null) {
                            var index = 0
                            if (lastNoti != null) {
                                for (i in chats.indices) {
                                    if (chats[i] == lastNoti.message) {
                                        index = i
                                        break
                                    }
                                }
                            }
                            for (i in index + 1..chats.size - 1) {
                                content = chats[i].toString()

                                Log.d("inboxxx", "onSave: " + content)

                                val mTitle = Title(
                                    XTools.getTitle(title),
                                    XTools.getDescription(title),
                                    sbn.packageName
                                )

                                saveTitle(titleDao, mTitle)

                                val notification = Notification(
                                    sbn.id,
                                    sbn.key,
                                    sbn.packageName,
                                    XTools.getTitle(title),
                                    XTools.getDescription(title),
                                    content,
                                    sbn.notification.category,
                                    postedTime = sbn.notification.`when`,
                                    channelId = channelId
                                )

                                saveNotification(
                                    notiDao,
                                    notification,
                                    icon = sbn.notification.largeIcon
                                )
                            }
                        }


                    }
                }

//                "android.app.Notification\$BigPictureStyle" -> {
//                    if (extras.containsKey(android.app.Notification.EXTRA_PICTURE)) {
//                        val picture = extras.get(android.app.Notification.EXTRA_PICTURE) as Bitmap
//
//                        content = if (extras.get("android.text") == null) "" else
//                            extras.get("android.text").toString().trim()
//
//                        if (title == "" && content == "") return
//
//                        val mTitle = Title(
//                            XTools.getTitle(title),
//                            XTools.getDescription(title),
//                            sbn.packageName
//                        )
//                        saveTitle(titleDao, mTitle)
//
//                        val notification = Notification(
//                            sbn.id,
//                            sbn.key,
//                            sbn.packageName,
//                            XTools.getTitle(title),
//                            XTools.getDescription(title),
//                            content,
//                            sbn.notification.category,
//                            postedTime = sbn.notification.`when`,
//                            channelId = channelId
//                        )
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            saveNotification(
//                                notiDao,
//                                notification,
//                                sbn.notification.largeIcon,
//                                picture
//                            )
//                        }
//                    }
//                }

                "android.app.Notification\$BigTextStyle" -> {
                    if (extras.containsKey(android.app.Notification.EXTRA_BIG_TEXT)) {
                        content = extras.get(android.app.Notification.EXTRA_BIG_TEXT).toString()
                    }

                    if (title == "" && content == "") return

                    val mTitle = Title(
                        XTools.getTitle(title),
                        XTools.getDescription(title),
                        sbn.packageName
                    )
                    saveTitle(titleDao, mTitle)

                    val notification = Notification(
                        sbn.id,
                        sbn.key,
                        sbn.packageName,
                        XTools.getTitle(title),
                        XTools.getDescription(title),
                        content,
                        sbn.notification.category,
                        postedTime = sbn.notification.`when`,
                        channelId = channelId
                    )
                    saveNotification(notiDao, notification, sbn.notification.largeIcon)
                }

                "android.app.Notification\$MessagingStyle" -> {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Log.d("msgsss", "======================= ")
                        val lastNoti = withContext(Dispatchers.IO) {
                            notiDao.getLastNotiOfGroup(sbn.key)
                        }

                        val b =
                            extras.get(android.app.Notification.EXTRA_MESSAGES) as Array<Parcelable>?

                        if (b != null) {

                            var titleConversion = extras.get(android.app.Notification.EXTRA_CONVERSATION_TITLE).toString().trim()

                            if (titleConversion == "null"){
                                titleConversion = if (extras.get("android.title") == null) "" else
                                    extras.get("android.title").toString().trim()
                            }

                            for (tmp in b) {
                                val msgBundle = tmp as Bundle
                                content = msgBundle.get("text").toString()
                                val time = msgBundle.get("time") as Long
                                val sender = msgBundle.get("sender").toString()

                                Log.d("msgsss", "onSave: " + content)
                                Log.d("msgsss", "onSave: " + time)
                                Log.d("msgsss", "onSave: " + sender)
                                Log.d("msgsss", "onSave: " + titleConversion)

                                val mTitle = Title(
                                    XTools.getTitle(titleConversion),
                                    sender,
                                    sbn.packageName
                                )

                                saveTitle(titleDao, mTitle)

                                val notification = Notification(
                                    sbn.id,
                                    sbn.key,
                                    sbn.packageName,
                                    XTools.getTitle(titleConversion),
                                    sender,
                                    content,
                                    sbn.notification.category,
                                    postedTime = time,
                                    channelId = channelId
                                )

                                saveNotification(
                                    notiDao,
                                    notification,
                                    icon = sbn.notification.largeIcon
                                )
                            }

                        } else {
                            Log.d("sss", "onNotificationPosted: message style has null content")
                        }
                    }
                }

                else -> {
                    content = if (extras.get("android.text") == null) "" else
                        extras.get("android.text").toString().trim()

                    if (title == "" && content == "") return

                    val mTitle = Title(
                        XTools.getTitle(title),
                        XTools.getDescription(title),
                        sbn.packageName
                    )
                    saveTitle(titleDao, mTitle)

                    val notification = Notification(
                        sbn.id,
                        sbn.key,
                        sbn.packageName,
                        XTools.getTitle(title),
                        XTools.getDescription(title),
                        content,
                        sbn.notification.category,
                        postedTime = sbn.notification.`when`,
                        channelId = channelId
                    )
                    saveNotification(notiDao, notification, sbn.notification.largeIcon)
                }
            }


            var remoteInput : List<RemoteInput>? = null
            val actions = sbn.notification.actions
            if (actions != null){
                for (a in actions){
                    if (a!= null && a.remoteInputs!= null){
                        remoteInput = a.remoteInputs.toList()
                        actionIntentMap.put(sbn.key,a.actionIntent)
                        Log.d("abccc", "onSave: " + remoteInput[0].resultKey)
                        break
                    }
                }
            }

            if (intentMap.size > 100) intentMap.clear()
            if (remoteInputMap.size > 100) remoteInputMap.clear()
            intentMap.put(sbn.key, sbn.notification.contentIntent)
            remoteInput?.let { it1 -> remoteInputMap.put(sbn.key, it1) }

        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onListenerConnected() {
        super.onListenerConnected()
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private suspend fun saveNotification(
        notiDao: NotificationDao,
        notification: Notification,
        icon: Bitmap? = null,
        image: Bitmap? = null
    ) {
        launch(Dispatchers.IO) {

            val iconPath = async {
                XTools.saveIcon(
                    applicationContext,
                    icon,
                    2,
                    notification.postedTime.toString()
                )
            }

            val imagePath = async {
                XTools.saveIcon(
                    applicationContext,
                    image,
                    3,
                    notification.postedTime.toString()
                )
            }

            notification.apply {
                this.iconPath = iconPath.await()
                this.largeImagePath = imagePath.await()
            }
            Log.d("noti listener", "saveNotification: ${notification.iconPath} ")
            notiDao.insertNoti(notification)
            Log.d("kkk", "onNotificationPosted: saved noti")
        }

    }

    private suspend fun saveTitle(titleDao: TitleDao, title: Title) {
        launch(Dispatchers.IO) {
            titleDao.insertTitle(title)
            Log.d("kkk", "onNotificationPosted: saved title")
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

}
