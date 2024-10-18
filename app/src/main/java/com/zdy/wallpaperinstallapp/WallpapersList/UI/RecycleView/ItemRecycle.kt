package com.zdy.wallpaperinstallapp.WallpapersList.UI.RecycleView

import com.zdy.wallpaperinstallapp.PickUpWallpaper.Objects.PickUpImage

sealed class ItemRecycle {


    class RecycleWallpaperItem(
        val image: PickUpImage
    ) : ItemRecycle()

    class RecycleButtonItem() : ItemRecycle()


}