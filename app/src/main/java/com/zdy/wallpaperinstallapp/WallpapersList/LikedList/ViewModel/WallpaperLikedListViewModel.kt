package com.zdy.wallpaperinstallapp.WallpapersList.LikedList.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.models.ObjectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import kotlinx.coroutines.launch

class WallpaperLikedListViewModel(
    private val application: Application,
    val imageRepository: ImagesRepository
) : AndroidViewModel(application) {


    fun saveWallpaper(wallpaper: LocalWallpaper) = viewModelScope.launch {
        imageRepository.insert(wallpaper)
    }

    fun getSavedWallpaper() = imageRepository.getSavedWallpaper()

    fun deleteWallpaper(wallpaper: LocalWallpaper) = viewModelScope.launch {
        imageRepository.delete(wallpaper)
    }

}