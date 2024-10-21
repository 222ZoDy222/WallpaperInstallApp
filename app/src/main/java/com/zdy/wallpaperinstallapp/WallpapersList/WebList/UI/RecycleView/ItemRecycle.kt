package com.zdy.wallpaperinstallapp.WallpapersList.WebList.UI.RecycleView

import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage

sealed class ItemRecycle {


    class RecycleWallpaperItem(
        val image: PickUpImage
    ) : ItemRecycle()

    class RecycleButtonItem() : ItemRecycle()


}