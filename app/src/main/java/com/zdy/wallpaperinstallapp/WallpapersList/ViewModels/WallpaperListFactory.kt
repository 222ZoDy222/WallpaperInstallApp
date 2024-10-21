package com.zdy.wallpaperinstallapp.WallpapersList.ViewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository

class WallpaperListFactory(
    val application: Application,
    val imageRepository: ImagesRepository
) : ViewModelProvider.AndroidViewModelFactory(application){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WallpaperListViewModel(application, imageRepository) as T
    }
}