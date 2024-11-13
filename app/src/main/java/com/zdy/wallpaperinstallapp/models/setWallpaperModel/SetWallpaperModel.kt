package com.zdy.wallpaperinstallapp.models.setWallpaperModel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SetWallpaperModel {


    private fun getMimeType(format: Bitmap.CompressFormat): Pair<String,String>{
        val (fileName, mimeType) = when (format) {
            Bitmap.CompressFormat.JPEG -> "$WALLPAPER_CACHE_FILE_NAME.jpg" to "image/jpeg"
            Bitmap.CompressFormat.PNG -> "$WALLPAPER_CACHE_FILE_NAME.png" to "image/png"
            Bitmap.CompressFormat.WEBP -> "$WALLPAPER_CACHE_FILE_NAME.webp" to "image/webp"
            else -> "$WALLPAPER_CACHE_FILE_NAME.png" to "image/png"
        }
        return Pair(fileName,mimeType)
    }
    private fun getBitmapUri(bitmap: Bitmap, context: Context, format: Bitmap.CompressFormat): Uri? {
        // Creating wallpaper image in cache directory
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()

        val fileMime = getMimeType(format)

        val file = File(cachePath, fileMime.first)
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(format, 100, fileOutputStream)
        fileOutputStream.close()

        // Получаем URI файла через FileProvider
        val bitmapUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        return bitmapUri
    }

    fun setWallpaperSettings(bitmap: Bitmap, context: Context, format: Bitmap.CompressFormat){

        try {

            val fileMime = getMimeType(format)
            val bitmapUri = getBitmapUri(bitmap,context,format)

            // Создаём интент для установки обоев
            val intent = Intent(Intent.ACTION_ATTACH_DATA).setDataAndType(
                bitmapUri,
                fileMime.second
            )
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .putExtra("mimeType", fileMime.second)

            context.startActivity(Intent.createChooser(intent, "Wallpaper settings"))

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun shareWallpaperSettings(bitmap: Bitmap, context: Context, format: Bitmap.CompressFormat) {

        val bitmapUri = getBitmapUri(bitmap,context,format)

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM,bitmapUri)
            type="image/*"
        }
        context.startActivity(shareIntent)

    }





    companion object{

        const val WALLPAPER_INTENT_TYPE = "image/jpeg"
        const val WALLPAPER_CACHE_FILE_NAME = "wallpaper."

    }


}