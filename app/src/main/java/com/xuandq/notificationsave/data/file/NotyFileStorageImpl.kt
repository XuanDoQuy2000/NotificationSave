package com.xuandq.notificationsave.data.file

import android.content.Context
import android.graphics.Bitmap
import com.xuandq.notificationsave.model.enum.IconType
import java.io.File
import java.io.FileOutputStream

class NotyFileStorageImpl(
    private val context: Context
) : NotyFileStorage {

    private fun getIconDirectoryPath(type: IconType) : String {
        return when (type){
            IconType.ICON_APP -> {
                context.filesDir.absolutePath + "/iconApp/"
            }

            IconType.ICON_NOTIFICATION -> {
                context.filesDir.absolutePath + "/iconNotification"
            }

            IconType.IMAGE_NOTIFICATION -> {
                context.filesDir.absolutePath + "/imageNotification"
            }
        }
    }

    override fun saveIcon(bitmap: Bitmap?, fileName: String, type: IconType) : String?{
        bitmap?.let {
            val dirPath = getIconDirectoryPath(type)
            val directory = File(dirPath)
            if (!directory.exists()) directory.mkdir()
            val file = File(directory, "$fileName.png")
            var out : FileOutputStream? = null
            try {
                file.createNewFile()
                out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                return file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                out?.close()
            }
        }
        return null
    }
}