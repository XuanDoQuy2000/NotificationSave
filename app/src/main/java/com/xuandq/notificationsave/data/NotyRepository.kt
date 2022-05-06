package com.xuandq.notificationsave.data

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import com.xuandq.notificationsave.data.database.AppDatabase
import com.xuandq.notificationsave.data.file.NotyFileStorage
import com.xuandq.notificationsave.data.file.NotyFileStorageImpl
import com.xuandq.notificationsave.data.share_pref.NotySharedPreferenceImpl
import com.xuandq.notificationsave.model.App
import com.xuandq.notificationsave.model.AppWithNoti
import com.xuandq.notificationsave.model.Notification
import com.xuandq.notificationsave.model.Title
import com.xuandq.notificationsave.model.enum.IconType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class NotyRepository(
    private val db: AppDatabase,
    private val pref: NotySharedPreferenceImpl,
    private val fileStorage: NotyFileStorageImpl
) : NotyFileStorage {
    suspend fun insertApp(app: App) = withContext(Dispatchers.IO) {
        db.appDao().insert(app)
    }

    suspend fun insertApps(apps: List<App>) {
        db.appDao().insert(apps)
    }

    suspend fun insertTitle(title: Title) = withContext(Dispatchers.IO) {
        db.titleDao().insert(title)
    }

    suspend fun insertTitles(titles: List<Title>) {
        db.titleDao().insert(titles)
    }

    suspend fun insertNoti(noti: Notification) = withContext(Dispatchers.IO) {
        db.notiDao().insert(noti)
    }

    suspend fun insertNoties(noties: List<Notification>) {
        db.notiDao().insert(noties)
    }

    suspend fun updateApp(app: App) {
        db.appDao().update(app)
    }

    suspend fun updateApps(apps: List<App>) {
        db.appDao().update(apps)
    }

    suspend fun updateTitle(title: Title) {
        db.titleDao().update(title)
    }

    suspend fun updateTitles(titles: List<Title>) {
        db.titleDao().update(titles)
    }

    suspend fun updateNoti(noti: Notification) {
        db.notiDao().update(noti)
    }

    suspend fun updateNoties(noties: List<Notification>) {
        db.notiDao().update(noties)
    }

    suspend fun getApp(packageName: String): App? {
        return db.appDao().getApp(packageName)
    }

    suspend fun getAppWithLastNoties(): List<AppWithNoti>? {
        return db.appWithNotiDao().getAppWithLastNoties()
    }

    override fun saveIcon(
        bitmap: Bitmap?,
        fileName: String,
        type: IconType
    ): String? {
        return fileStorage.saveIcon(bitmap, fileName, type)
    }

    suspend fun creatAppAsync(context: Context, packageName: String): App {
        return withContext(Dispatchers.Main) {
            val packageManager = context.packageManager
            val info = packageManager.getApplicationInfo(packageName, 0)
            val label = info.loadLabel(packageManager).toString()
            val icon = info.loadIcon(packageManager)
            withContext(Dispatchers.IO) {
                val iconPath = saveIcon(
                    icon.toBitmap(),
                    packageName,
                    IconType.ICON_APP
                )
                App(packageName, iconPath, label)
            }
        }
    }

    suspend fun getLastNotiOfGroupByKey(key: String) = withContext(Dispatchers.IO) {
        return@withContext db.notiDao().getLastNotiOfGroup(key)
    }

    suspend fun addNotification(
        notification: Notification,
        icon: Bitmap? = null,
        image: Bitmap? = null
    ) {
        withContext(Dispatchers.IO){
            val iconPath = async {
                saveIcon(
                    icon,
                    "${notification.content.take(10)}_${notification.timeStamp}",
                    IconType.ICON_NOTIFICATION
                )
            }

            val imagePath = async {
                saveIcon(
                    image,
                    "${notification.content.take(10)}_${notification.timeStamp}",
                    IconType.IMAGE_NOTIFICATION
                )
            }
            notification.apply {
                this.iconPath = iconPath.await()
                this.largeImagePath = imagePath.await()
            }
            insertNoti(notification)
        }
    }

}