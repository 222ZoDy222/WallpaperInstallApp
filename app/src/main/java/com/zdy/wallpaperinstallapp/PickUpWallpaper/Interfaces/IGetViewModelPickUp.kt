package com.zdy.wallpaperinstallapp.PickUpWallpaper.Interfaces

import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.PickUpWallpaperViewModel
import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.SetWallpaperViewModel

interface IGetViewModelPickUp {
    fun getViewModelPickUp() : PickUpWallpaperViewModel

    fun getViewModelSet() : SetWallpaperViewModel
}