package com.zdy.wallpaperinstallapp.inheritance

import androidx.appcompat.app.AppCompatActivity
import com.zdy.wallpaperinstallapp.DB.WallpaperDatabase
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository

open class WallpaperActivity : AppCompatActivity() {


    protected val imagesRepository: ImagesRepository by lazy {
        val repository = ImagesRepository(WallpaperDatabase(this))
        repository
    }

}