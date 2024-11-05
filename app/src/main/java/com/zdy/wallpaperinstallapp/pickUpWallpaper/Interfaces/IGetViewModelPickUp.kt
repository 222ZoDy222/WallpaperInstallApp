package com.zdy.wallpaperinstallapp.pickUpWallpaper.Interfaces

import com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel.PickUpWallpaperViewModel
import com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel.SetWallpaperViewModel

interface IGetViewModelPickUp {
    fun getViewModelPickUp() : PickUpWallpaperViewModel

    fun getViewModelSet() : SetWallpaperViewModel
}