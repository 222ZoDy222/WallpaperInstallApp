package com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.wallpapersList.LikedList.ViewModel.WallpaperLikedListViewModel

class PickUpWallpaperViewModelFactory(
    val application: Application,
    val likedListViewModel: WallpaperLikedListViewModel
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PickUpWallpaperViewModel(application, likedListViewModel) as T
    }
}