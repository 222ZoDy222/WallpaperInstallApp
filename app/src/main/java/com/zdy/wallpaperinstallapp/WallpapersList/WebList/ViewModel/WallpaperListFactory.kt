package com.zdy.wallpaperinstallapp.WallpapersList.WebList.ViewModel

import android.app.Application
import android.provider.MediaStore.Images
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository

class WallpaperListFactory(
    val application: Application,
    val imageRepository: ImagesRepository
) : ViewModelProvider.AndroidViewModelFactory(application){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WallpaperListViewModel(application, imageRepository) as T
    }
}