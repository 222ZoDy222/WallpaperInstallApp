package com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel

import android.app.Activity
import android.app.Application
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.models.SetWallpaperModel.WallpaperSetter
import kotlinx.coroutines.launch
import java.security.AccessController.getContext


class SetWallpaperViewModel(application: Application) : AndroidViewModel(application) {




    fun setWallpaper(image: Bitmap, matrix: Matrix, context: Context) = viewModelScope.launch {

        // TODO: Make it !!!
        WallpaperSetter.setHomeWallpaper(image.toDrawable(context.resources),context)
        return@launch
        val imageWidth = image.width
        val imageHeight = image.height
        val screenWidth : Int
        val screenHeight : Int

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
             val metrics =(context as Activity).windowManager.currentWindowMetrics
             screenWidth = metrics.bounds.width()
             screenHeight = metrics.bounds.height()
        } else@Suppress("DEPRECATION") {

            val display = (context as Activity).windowManager.defaultDisplay
             screenWidth = display.width
             screenHeight = display.height
         }

        val values = FloatArray(9)
        matrix.getValues(values)

        val visibleRect = getWallpaperRect(imageWidth, imageHeight, screenWidth, screenHeight, matrix)

        setWallpaper(image,visibleRect)
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
            var t = 0
        }


    }

    // Метод для получения смещённой области (Rect), которая будет использована для обоев
    private fun getWallpaperRect(imageWidth: Int, imageHeight: Int, viewWidth: Int, viewHeight: Int, matrix : Matrix): Rect {
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

}