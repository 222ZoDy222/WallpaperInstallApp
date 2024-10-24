package com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel

import android.app.Application
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.utils.getBitmapFormat
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.Format


class SetWallpaperViewModel(application: Application) : AndroidViewModel(application) {




    fun setWallpaper(image: PickUpImage, context: Context) = viewModelScope.launch {


        val format = image.url?.let {

        }
        if(format != null){
            image.bitmap?.let {
                SetWallpaperSettings(it,context, image.url.getBitmapFormat()!!)
            }
        } else{
            // TODO: Exception Unknown Format of image
        }


        // First variant of setting wallpaper that crop image on Huawei
//        val imageWidth = image.width
//        val imageHeight = image.height
//        val screenWidth : Int
//        val screenHeight : Int
//
//         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//             val metrics =(context as Activity).windowManager.currentWindowMetrics
//             screenWidth = metrics.bounds.width()
//             screenHeight = metrics.bounds.height()
//        } else@Suppress("DEPRECATION") {
//
//            val display = (context as Activity).windowManager.defaultDisplay
//             screenWidth = display.width
//             screenHeight = display.height
//         }
//
//        val values = FloatArray(9)
//        matrix.getValues(values)
//
//        val visibleRect = getWallpaperRect(imageWidth, imageHeight, screenWidth, screenHeight, matrix)
//
//        setWallpaper(image,visibleRect)
    }


    private fun setWallpaper(image: Bitmap, rect: Rect? = null) = viewModelScope.launch {

        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && rect != null) {
                WallpaperManager.getInstance(getApplication<Application>().applicationContext).setBitmap(image
                    , rect, true, WallpaperManager.FLAG_SYSTEM
                )
            } else{
                WallpaperManager.getInstance(getApplication<Application>().applicationContext).setBitmap(image)
            }

            // TODO: Alert Dialog - Обои установлены
        } catch (ex: Exception){
            // TODO: Alert Dialog - Ошибка при установке обоев

        }


    }

    private fun SetWallpaperSettings(bitmap: Bitmap, context: Context, format: Bitmap.CompressFormat){

        try {
            // Creating wallpaper image in cache directory
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()

            val (fileName, mimeType) = when (format) {
                Bitmap.CompressFormat.JPEG -> "$WALLPAPER_CACHE_FILE_NAME.jpg" to "image/jpeg"
                Bitmap.CompressFormat.PNG -> "$WALLPAPER_CACHE_FILE_NAME.png" to "image/png"
                Bitmap.CompressFormat.WEBP -> "$WALLPAPER_CACHE_FILE_NAME.webp" to "image/webp"
                else -> "$WALLPAPER_CACHE_FILE_NAME.png" to "image/png"
            }

            val file = File(cachePath, fileName)
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(format, 100, fileOutputStream)
            fileOutputStream.close()

            // Получаем URI файла через FileProvider
            val bitmapUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            // Создаём интент для установки обоев
            val intent = Intent(Intent.ACTION_ATTACH_DATA).setDataAndType(
                bitmapUri,
                mimeType
            )
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .putExtra("mimeType", mimeType)

            context.startActivity(Intent.createChooser(intent, "Wallpaper settings"))

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    // Метод для получения смещённой области (Rect), которая будет использована для обоев
    private fun getWallpaperRect(viewWidth: Int, viewHeight: Int, matrix : Matrix): Rect {
        // Получаем текущие значения матрицы (масштаб и смещение)
        val values = FloatArray(9)
        matrix.getValues(values)

        val scale = values[Matrix.MSCALE_X]
        val translateX = values[Matrix.MTRANS_X]
        val translateY = values[Matrix.MTRANS_Y]

        // Рассчитываем видимую область изображения
        val visibleRect = RectF(
            -translateX / scale,
            -translateY / scale,
            (viewWidth - translateX) / scale,
            (viewHeight - translateY) / scale
        )

        // Преобразуем в целочисленный Rect
        return Rect(
            visibleRect.left.toInt(),
            visibleRect.top.toInt(),
            visibleRect.right.toInt(),
            visibleRect.bottom.toInt()
        )
    }


    companion object{

        const val WALLPAPER_INTENT_TYPE = "image/jpeg"
        const val WALLPAPER_CACHE_FILE_NAME = "wallpaper."

    }

}