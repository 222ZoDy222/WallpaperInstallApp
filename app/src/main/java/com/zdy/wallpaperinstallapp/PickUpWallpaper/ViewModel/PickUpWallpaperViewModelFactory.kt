package com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.WallpapersList.LikedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.RecycleView.ViewModel.RecycleViewModel

class PickUpWallpaperViewModelFactory(
    val application: Application,
    val likedListViewModel: WallpaperLikedListViewModel
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PickUpWallpaperViewModel(application, likedListViewModel) as T
    }
}