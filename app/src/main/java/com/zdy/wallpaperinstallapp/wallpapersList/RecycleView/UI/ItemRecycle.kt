package com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.UI

import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage

sealed class ItemRecycle {


    class RecycleWallpaperItem(
        val image: PickUpImage
    ) : ItemRecycle()

    class RecycleButtonItem() : ItemRecycle()


}