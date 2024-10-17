package com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel

import android.app.Application
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SetWallpaperViewModel(application: Application) : AndroidViewModel(application) {



    fun setWallpaper(image: Bitmap, rect: Rect? = null) = viewModelScope.launch {

        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && rect != null) {
                WallpaperManager.getInstance(getApplication<Application>().applicationContext).setBitmap(image
                    , rect, true, WallpaperManager.FLAG_SYSTEM
                )
            } else{
                WallpaperManager.getInstance(getApplication<Application>().applicationContext).setBitmap(image)
            }
        } catch (ex: Exception){
            // TODO: ошибка при установке обоев
        }


    }



}