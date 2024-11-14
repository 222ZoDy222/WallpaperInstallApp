package com.zdy.wallpaperinstallapp.utils.extensions

import android.os.Build
import android.os.Bundle
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.SelectWallpaperActivity
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage

fun Bundle?.getParcelableImage(tag: String): PickUpImage? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return this?.getParcelable(tag, PickUpImage::class.java)
    } else{
        @Suppress("DEPRECATION")
        return this?.getParcelable<PickUpImage>(tag)
    }
}