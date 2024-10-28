package com.zdy.wallpaperinstallapp.models.LocalSave

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.utils.getImageFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.MessageDigest

class BitmapSaveManager {

    companion object{

        private const val DATA_FOLDER = ".savedWallpaper"


        suspend fun saveImageWallpaper(image: PickUpImage, context: Context){
            if(image.url != null ){
                image.bitmap?.let {
                    saveImageToHiddenFolder(context, it,image.url)
                }
            }
        }

        suspend fun loadImageWallpaper(image: PickUpImage, context: Context) : Bitmap?{
            return image.url?.let { loadImageFromHiddenFolder(context, it) }
        }

        suspend fun deleteImageWallpaper(image: PickUpImage, context: Context){
            image.url?.let { deleteImageFromHiddenFolder(context, it) }
        }


        private fun getHiddenFolder(context: Context): File {
            val folder = File(context.filesDir, DATA_FOLDER)
            if (!folder.exists()) {
                folder.mkdir()
            }
            return folder
        }

        private fun generateHash(url: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(url.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        private suspend fun saveImageToHiddenFolder(context: Context, bitmap: Bitmap, url: String): Boolean {
            val format = url.getImageFormat() ?: "jpg"
            val fileName = generateHash(url) + ".$format"
            val folder = getHiddenFolder(context)
            val file = File(folder, fileName)

            return withContext(Dispatchers.IO) {
                try {
                    FileOutputStream(file).use { outputStream ->
                        val compressFormat = when (format) {
                            "png" -> Bitmap.CompressFormat.PNG
                            "webp" -> Bitmap.CompressFormat.WEBP
                            "bmp" -> Bitmap.CompressFormat.PNG // Bitmap didn't work with BMP
                            else -> Bitmap.CompressFormat.JPEG // Default format
                        }
                        bitmap.compress(compressFormat, 100, outputStream)
                    }
                    true
                } catch (e: IOException) {
                    false
                }
            }
        }

        private suspend fun loadImageFromHiddenFolder(context: Context, url: String): Bitmap? {
            val format = url.getImageFormat() ?: "jpg"
            val fileName = generateHash(url) + ".$format"
            val folder = getHiddenFolder(context)
            val file = File(folder, fileName)

            return withContext(Dispatchers.IO) {
                if (file.exists()) {
                    BitmapFactory.decodeFile(file.absolutePath)
                } else {
                    null
                }
            }
        }

        private suspend fun deleteImageFromHiddenFolder(context: Context, url: String): Boolean {
            val format = url.getImageFormat() ?: "jpg"
            val fileName = generateHash(url) + ".$format"
            val folder = getHiddenFolder(context)
            val file = File(folder, fileName)

            return withContext(Dispatchers.IO) {
                if (file.exists()) {
                    file.delete()
                } else {
                    false
                }
            }
        }

    }

}


private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}