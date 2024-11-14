package com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.models.setWallpaperModel.SetWallpaperModel
import com.zdy.wallpaperinstallapp.utils.extensions.getBitmapFormat
import kotlinx.coroutines.launch


open class SetWallpaperViewModel() : ViewModel() {


    private val setWallpaperModel = SetWallpaperModel()

    fun shareWallpaper(image: PickUpImage, context: Context){
        val format = image.url.getBitmapFormat()
        if (format != null) {
            image.bitmap?.let { setWallpaperModel.shareWallpaperSettings(it,context,format) }
        }
    }

    fun setWallpaper(image: PickUpImage, context: Context) = viewModelScope.launch {

        val format = image.url.getBitmapFormat()
        if(format != null){
            image.bitmap?.let {
                setWallpaperModel.setWallpaperSettings(it,context, format)
            }
        }
    }




}