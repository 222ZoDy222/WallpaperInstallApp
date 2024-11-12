package com.zdy.wallpaperinstallapp.activity.likedList.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository

@Suppress("UNCHECKED_CAST")
class WallpaperLikedListFactory(
    val application: Application,
    val imageRepository: ImagesRepository
) : ViewModelProvider.AndroidViewModelFactory(application){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WallpaperLikedListViewModel(imageRepository) as T
    }
}