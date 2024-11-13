package com.zdy.wallpaperinstallapp.utils.extensions

import android.os.Build
import android.os.Bundle
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.SelectWallpaperActivity
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage

fun Bundle?.getParcelableImage(): PickUpImage? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return this?.getParcelable(SelectWallpaperActivity.WALLPAPER_TAG, PickUpImage::class.java)
    } else{
        @Suppress("DEPRECATION")
        return this?.getParcelable<PickUpImage>(SelectWallpaperActivity.WALLPAPER_TAG)
    }
}