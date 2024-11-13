package com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.models.SetWallpaperModel.SetWallpaperModel
import com.zdy.wallpaperinstallapp.utils.getBitmapFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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