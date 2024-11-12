package com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.activity.likedList.ViewModel.WallpaperLikedListViewModel

class RecycleViewModelFactory(
    val application: Application,
    val likedListViewModel: WallpaperLikedListViewModel
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecycleViewModel(application, likedListViewModel) as T
    }
}