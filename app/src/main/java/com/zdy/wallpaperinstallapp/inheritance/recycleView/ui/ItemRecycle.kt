package com.zdy.wallpaperinstallapp.inheritance.recycleView.ui

import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage

sealed class ItemRecycle {


    class RecycleWallpaperItem(
        val image: PickUpImage
    ) : ItemRecycle()

    class RecycleButtonItem() : ItemRecycle()


}