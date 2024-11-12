package com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.activity.likedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.activity.webList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.models.LocalSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import kotlinx.coroutines.launch
import java.io.File

class PickUpWallpaperViewModel(
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    private val localSaveModel: LocalSaveModel = LocalSaveModel(imagesRepository)

    var selectedImage : MutableLiveData<PickUpImage> = MutableLiveData()

    private var backgroundDrawable : MutableLiveData<Drawable> = MutableLiveData()
    fun getBackgroundDrawable() = backgroundDrawable


    fun onLikeImage(context: Context){
        selectedImage.value?.let {
            viewModelScope.launch {
                localSaveModel.onLikeClicked(selectedImage.value!!, context)
                selectedImage.value = selectedImage.value
            }
        }
    }

    fun SelectImage(image : PickUpImage, context: Context){
        val bitmap = loadBitmapFromFile(context)
        bitmap?.let {
            image.bitmap = it
        }
        deleteSavedImage(context)
        selectedImage.value = image

        setBackgroundImage(context)
    }


    private fun loadWeb(image: PickUpImage, context: Context){
        ImagesRepository.LoadBitmapByURL(context,image.url, object : CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                selectedImage.value?.bitmap = resource
                backgroundDrawable.value = resource.toDrawable(context.resources)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}

            override fun onLoadFailed(errorDrawable: Drawable?) {
                loadWeb(image, context)
            }
        })
    }

    private fun setBackgroundImage(context: Context){

        if(selectedImage.value != null){
            if(selectedImage.value!!.bitmap != null){

                val drawable = BitmapDrawable(
                    context.resources,
                    selectedImage.value!!.bitmap)

                backgroundDrawable.value = drawable

            } else{
                loadWeb(selectedImage.value!!, context)

            }
        }
    }

    // Set background after getting image by URL
    fun SetBackgroundImage(bitmap: Bitmap, context: Context){
        selectedImage.value?.bitmap = bitmap
        backgroundDrawable.value = BitmapDrawable(
            context.resources,
            bitmap
        )
    }


    // If user clicked so fast ui must update and resize
    fun updateDrawableImage(){
        if(backgroundDrawable.value != null){
            backgroundDrawable.value = backgroundDrawable.value
        }
    }

    fun deleteSavedImage(context: Context){
        val file = File(context.cacheDir, WallpaperListViewModel.SELECTED_IMAGE_NAME)
        if (file.exists()) {
            file.delete()
        }
    }

    fun loadBitmapFromFile(context: Context): Bitmap? {
        val file = File(context.cacheDir, WallpaperListViewModel.SELECTED_IMAGE_NAME)
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

    fun setImageToFullScreen(drawable: Drawable, context: Context){
        val imageWidth = drawable.intrinsicWidth
        val imageHeight = drawable.intrinsicHeight

        val viewWidth = context.resources.displayMetrics.widthPixels
        val viewHeight = context.resources.displayMetrics.heightPixels

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