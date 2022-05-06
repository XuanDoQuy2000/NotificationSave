package com.xuandq.notificationsave.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.LauncherActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.core.graphics.drawable.toBitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.xuandq.notificationsave.model.enum.IconType
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Parcelize
@Entity
data class App (
    @PrimaryKey
    val packageName : String,
    val iconPath : String? = null,
    val label : String? = null,
    var isBlockNoti : Boolean = false,
    var isSaveNoti : Boolean = true,
    var isMessager : Boolean = false
) : Parcelable{
    constructor(pm: PackageManager, info: ResolveInfo, iconPath: String) : this(
        info.activityInfo.packageName,
        iconPath,
        info.loadLabel(pm).toString()
    )

    @SuppressLint("NewApi")
    constructor(pm: PackageManager, info: LauncherActivityInfo, iconPath: String) : this(
        info.componentName.packageName,
        iconPath,
        info.label.toString()
    )

    companion object {

    }
}