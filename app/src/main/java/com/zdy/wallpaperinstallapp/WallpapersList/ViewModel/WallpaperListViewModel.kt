package com.zdy.wallpaperinstallapp.WallpapersList.ViewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WallpaperListViewModel(application: Application) : AndroidViewModel(application){


    val menuLiveData = MutableLiveData<menu>(menu.wallpaperInternet)

    init {

        // TODO: Start Load Internet Wallpaper

        // TODO: Start Load ROOM wallpaper

    }


    enum class menu {
        wallpaperInternet,
        wallpaperLiked,
    }

}