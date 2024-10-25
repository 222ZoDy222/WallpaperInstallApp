package com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.WallpapersList.LikedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import java.io.File

class PickUpWallpaperViewModel(
    private val application: Application,
    private val likedListViewModel: WallpaperLikedListViewModel
) : AndroidViewModel(application) {


    var selectedImage : MutableLiveData<PickUpImage> = MutableLiveData()

    private var backgroundDrawable : MutableLiveData<Drawable> = MutableLiveData()
    fun getBackgroundDrawable() = backgroundDrawable


    fun onLikeImage(){
        selectedImage.value?.let {
            likedListViewModel.onLikeClicked(selectedImage.value!!)
            selectedImage.value = selectedImage.value
            var t = 0
        }
    }

    fun SelectImage(image : PickUpImage){
        val bitmap = loadBitmapFromFile()
        bitmap?.let {
            image.bitmap = it
        }
        deleteSavedImage()
        selectedImage.value = image

        setBackgroundImage()
    }


    private fun setDrawableImage(image: PickUpImage){

        if(image.bitmap != null){
            backgroundDrawable.value = image.bitmap!!.toDrawable(application.resources)
        }
        else {
            loadWeb(image)
        }

    }

    private fun loadWeb(image: PickUpImage){
        ImagesRepository.LoadBitmapByURL(application.applicationContext,image.url, object : CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                selectedImage.value?.bitmap = resource
                backgroundDrawable.value = resource.toDrawable(application.resources)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}

        })
    }

    private fun setBackgroundImage(){

        if(selectedImage.value != null){
            if(selectedImage.value!!.bitmap != null){

                val drawable = BitmapDrawable(
                    getApplication<Application>().applicationContext.resources,
                    selectedImage.value!!.bitmap)

                backgroundDrawable.value = drawable

            } else{
                loadWeb(selectedImage.value!!)

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

    fun setImageToFullScreen(drawable: Drawable){
        val imageWidth = drawable.intrinsicWidth
        val imageHeight = drawable.intrinsicHeight

        val viewWidth = application.applicationContext.resources.displayMetrics.widthPixels
        val viewHeight = application.applicationContext.resources.displayMetrics.heightPixels

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