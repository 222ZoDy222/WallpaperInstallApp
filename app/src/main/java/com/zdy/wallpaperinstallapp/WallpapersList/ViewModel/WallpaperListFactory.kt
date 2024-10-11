package com.zdy.wallpaperinstallapp.WallpapersList.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WallpaperListFactory(val application: Application) : ViewModelProvider.AndroidViewModelFactory(application){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WallpaperListViewModel(application) as T
    }
}