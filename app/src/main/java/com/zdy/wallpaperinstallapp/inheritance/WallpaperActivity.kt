package com.zdy.wallpaperinstallapp.inheritance

import androidx.appcompat.app.AppCompatActivity
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class WallpaperActivity : AppCompatActivity() {

    @Inject
    lateinit var imagesRepository: ImagesRepository

}