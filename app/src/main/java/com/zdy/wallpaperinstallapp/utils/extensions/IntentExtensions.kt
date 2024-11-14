package com.zdy.wallpaperinstallapp.utils.extensions

import android.content.Intent
import android.os.Build
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.SelectWallpaperActivity
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage


fun Intent?.getPickUpImage(tag: String) : PickUpImage?{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return this?.getParcelableExtra(SelectWallpaperActivity.WALLPAPER_TAG, PickUpImage::class.java)
    } else {
        @Suppress("DEPRECATION")
        return this?.getParcelableExtra(SelectWallpaperActivity.WALLPAPER_TAG)
    }
}