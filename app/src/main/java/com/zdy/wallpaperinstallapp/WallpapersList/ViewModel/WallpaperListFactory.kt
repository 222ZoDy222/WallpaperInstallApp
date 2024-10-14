package com.zdy.wallpaperinstallapp.WallpapersList.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.Web.Requests.ImageRepository

class WallpaperListFactory(
    val application: Application,
    val imageRepository: ImageRepository
) : ViewModelProvider.AndroidViewModelFactory(application){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WallpaperListViewModel(imageRepository) as T
    }
}