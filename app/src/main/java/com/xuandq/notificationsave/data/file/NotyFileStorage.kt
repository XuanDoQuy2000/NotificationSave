package com.xuandq.notificationsave.data.file

import android.content.Context
import android.graphics.Bitmap
import com.xuandq.notificationsave.model.enum.IconType

interface NotyFileStorage {
    fun saveIcon(bitmap: Bitmap?, fileName: String, type: IconType) : String?
}