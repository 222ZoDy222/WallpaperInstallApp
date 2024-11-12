package com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.activity.likedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.models.LocalSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository

class RecycleViewModelFactory(
    val application: Application,
    val repository: ImagesRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val localSaveModel = LocalSaveModel(repository)
        return RecycleViewModel(application, localSaveModel) as T
    }
}