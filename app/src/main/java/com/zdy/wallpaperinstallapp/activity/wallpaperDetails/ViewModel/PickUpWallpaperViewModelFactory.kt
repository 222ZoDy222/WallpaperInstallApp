package com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.activity.likedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository

class PickUpWallpaperViewModelFactory(
    application: Application,
    val imageRepository: ImagesRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PickUpWallpaperViewModel(imageRepository) as T
    }
}