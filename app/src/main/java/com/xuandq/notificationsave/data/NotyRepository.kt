package com.xuandq.notificationsave.data

import android.content.Context
import android.content.Intent
import android.content.pm.LauncherActivityInfo
import android.content.pm.LauncherApps
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.os.Build
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
import kotlinx.coroutines.*

class NotyRepository(
    val db: AppDatabase,
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

    suspend fun getAllApp(): List<App>? {
        return db.appDao().getAllApp()
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
        withContext(Dispatchers.IO) {
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

    suspend fun getInstalledApps(context: Context): List<App> = coroutineScope {
        val listApp = ArrayList<App>()
        val packageManager = context.packageManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val launcherApps =
                context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
            val profiles = launcherApps.profiles
            for (userHandle in profiles) {
                val apps =
                    launcherApps.getActivityList(null, userHandle)
                for (info in apps) {
                    launch {
                        listApp.add(convertToApp(context, info))
                    }
                }
            }
        } else {
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val activitiesInfo =
                packageManager.queryIntentActivities(intent, 0)
            for (info in activitiesInfo) {
                launch {
                    listApp.add(convertToApp(context, info))
                }
            }
        }
        return@coroutineScope listApp
    }

    private suspend fun convertToApp(context: Context, info: ResolveInfo): App {
        val pm = context.packageManager
        val icon = info.loadIcon(pm)
        val bitmap = icon.toBitmap()
        val label = info.loadLabel(pm).toString()
        val packageName = info.activityInfo.packageName
        val iconPath = withContext(Dispatchers.IO) {
            saveIcon(bitmap, packageName, IconType.ICON_APP)
        }
        return App(packageName, iconPath, label)
    }

    private suspend fun convertToApp(context: Context, info: LauncherActivityInfo): App {
        val icon = info.getIcon(0)
        val bitmap = icon.toBitmap()
        val label = info.label.toString()
        val packageName = info.componentName.packageName
        val iconPath = withContext(Dispatchers.IO) {
            saveIcon(bitmap, packageName, IconType.ICON_APP)
        }
        return App(packageName, iconPath, label)
    }

    fun isFirstLaunch() = pref.isFirstLaunch()

    fun setFirstLaunch(value : Boolean) {
        pref.setFirstLaunch(value)
    }

    suspend fun getListTitleWithNoti(packageName: String) = db.titleWithNotiDao().getListTitleWithNoti(packageName)

    fun fetchListNotiesOfTitle(packageName: String, title: String) = db.notiDao().fetchNotiByTitle(packageName, title)

    fun fetchAllNoties() = db.notiDao().fetchAll()
}