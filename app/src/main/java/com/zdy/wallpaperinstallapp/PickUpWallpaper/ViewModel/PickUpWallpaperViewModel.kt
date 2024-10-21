package com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.ViewModel.WallpaperListViewModel
import java.io.File

class PickUpWallpaperViewModel(application: Application) : AndroidViewModel(application) {


    var selectedImage : MutableLiveData<PickUpImage> = MutableLiveData()

    private var backgroundDrawable : MutableLiveData<Drawable> = MutableLiveData()

    fun SelectImage(image : PickUpImage){
        val bitmap = loadBitmapFromFile()
        bitmap?.let {
            image.bitmap = it
        }
        deleteSavedImage()

        selectedImage.value = image
        setBackgroundImage()
    }

    fun getUrl(): String? {
        return selectedImage.value?.url
    }



    fun getBackgroundDrawable() = backgroundDrawable

    private fun setBackgroundImage(){

        if(selectedImage.value != null){
            if(selectedImage.value!!.bitmap != null){

                val drawable = BitmapDrawable(
                    getApplication<Application>().applicationContext.resources,
                    selectedImage.value!!.bitmap)

                backgroundDrawable.value = drawable

            } else{
                // Load image by URL
                backgroundDrawable.value = null

            }
        }
    }

    // Set background after getting image by URL
    fun SetBackgroundImage(bitmap: Bitmap){
        selectedImage.value?.bitmap = bitmap
        backgroundDrawable.value = BitmapDrawable(
            getApplication<Application>().applicationContext.resources,
            bitmap
        )
    }


    // If user clicked so fast ui must update and resize
    fun updateDrawableImage(){
        if(backgroundDrawable.value != null){
            backgroundDrawable.value = backgroundDrawable.value
        }
    }

    fun deleteSavedImage(){
        val file = File(getApplication<Application>().cacheDir, WallpaperListViewModel.SELECTED_IMAGE_NAME)
        if (file.exists()) {
            file.delete()
        }
    }

    fun loadBitmapFromFile(): Bitmap? {
        val file = File(getApplication<Application>().cacheDir, WallpaperListViewModel.SELECTED_IMAGE_NAME)
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }


    // Matrix moving image
    private val matrix = Matrix()
    fun getMatrix() = matrix

    // Restrictions by moving
    private var maxX = 0f
    private var maxY = 0f

    // last touch
    private var lastX = 0f
    private var lastY = 0f

    fun setLastTouch(eventX: Float, eventY: Float) {
        lastX = eventX
        lastY = eventY
    }

    // Обработка движения и обновление матрицы
    fun handleMove(eventX: Float, eventY: Float): Matrix {
        val dx = eventX - lastX
        val dy = eventY - lastY

        // Получаем текущие значения матрицы
        val values = FloatArray(9)
        matrix.getValues(values)
        val currentX = values[Matrix.MTRANS_X]
        val currentY = values[Matrix.MTRANS_Y]

        // Ограничиваем перемещение
        val newX = currentX + dx
        val newY = currentY + dy

        if (newX <= 0 && newX >= maxX) {
            matrix.postTranslate(dx, 0f)
        }
        if (newY <= 0 && newY >= maxY) {
            matrix.postTranslate(0f, dy)
        }

        lastX = eventX
        lastY = eventY

        return matrix
    }

    // Начальная установка изображения на полный экран
    fun setImageToFullScreen(imageWidth: Int, imageHeight: Int, viewWidth: Int, viewHeight: Int) {
        val scaleX = viewWidth.toFloat() / imageWidth.toFloat()
        val scaleY = viewHeight.toFloat() / imageHeight.toFloat()
        val scale = maxOf(scaleX, scaleY)

        val dx = (viewWidth - imageWidth * scale) / 2
        val dy = (viewHeight - imageHeight * scale) / 2

        matrix.setScale(scale, scale)
        matrix.postTranslate(dx, dy)

        // Устанавливаем максимальные смещения по осям
        maxX = viewWidth - imageWidth * scale
        maxY = viewHeight - imageHeight * scale
    }




}