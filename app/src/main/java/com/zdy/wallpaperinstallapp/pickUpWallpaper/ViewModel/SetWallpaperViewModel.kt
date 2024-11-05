package com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel

import android.app.AlertDialog
import android.app.Application
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.utils.getBitmapFormat
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class SetWallpaperViewModel(
    private val application: Application,
) : AndroidViewModel(application) {




    fun ShareWallpaper(image: PickUpImage, context: Context){
        

        val format = image.url.getBitmapFormat()
        if (format != null) {
            image.bitmap?.let { shareWallpaperSettings(it,context,format) }
        } else{
            ShowAlertDialog(context.getString(R.string.error),
                context.getString(R.string.unknown_format_of_image),
                context)
        }
    }

    fun setWallpaper(image: PickUpImage, context: Context) = viewModelScope.launch {


        val format = image.url.getBitmapFormat()
        if(format != null){
            image.bitmap?.let {
                SetWallpaperSettings(it,context, format)
            }
        } else{
            ShowAlertDialog(context.getString(R.string.error),
                context.getString(R.string.unknown_format_of_image), context)
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


        } catch (ex: Exception){

        }


    }


    private fun getMimeType(format: CompressFormat): Pair<String,String>{
        val (fileName, mimeType) = when (format) {
            Bitmap.CompressFormat.JPEG -> "$WALLPAPER_CACHE_FILE_NAME.jpg" to "image/jpeg"
            Bitmap.CompressFormat.PNG -> "$WALLPAPER_CACHE_FILE_NAME.png" to "image/png"
            Bitmap.CompressFormat.WEBP -> "$WALLPAPER_CACHE_FILE_NAME.webp" to "image/webp"
            else -> "$WALLPAPER_CACHE_FILE_NAME.png" to "image/png"
        }
        return Pair(fileName,mimeType)
    }
    private fun getBitmapUri(bitmap: Bitmap, context: Context, format: CompressFormat): Uri? {
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

    private fun SetWallpaperSettings(bitmap: Bitmap, context: Context, format: Bitmap.CompressFormat){

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

    private fun shareWallpaperSettings(bitmap: Bitmap, context: Context, format: CompressFormat ) {


        val bitmapUri = getBitmapUri(bitmap,context,format)

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM,bitmapUri)
            type="image/*"
        }
        context.startActivity(shareIntent)

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


    private fun ShowAlertDialog(title: String, message: String, context: Context){
        val alertDialog = AlertDialog.Builder(context)
        val textView = TextView(context)
        textView.setPadding(40, 30, 20, 30);
        textView.textSize = 24f
        textView.text = title
        textView.setTextColor(application.resources.getColor(R.color.white));
        alertDialog.apply {

            setCustomTitle(textView)
            setTitle(title)
            setMessage(message)
            setNeutralButton(context.getString(R.string.ok)) { _, _ ->

            }

        }.create().show()
    }

    enum class alertMessageType{
        unknownFormat,
        errorSetWallpaper,
        wallpaperSetCompleted
    }

    companion object{

        const val WALLPAPER_INTENT_TYPE = "image/jpeg"
        const val WALLPAPER_CACHE_FILE_NAME = "wallpaper."

    }

}