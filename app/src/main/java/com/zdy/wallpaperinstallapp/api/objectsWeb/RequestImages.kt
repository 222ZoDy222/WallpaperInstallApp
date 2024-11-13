package com.zdy.wallpaperinstallapp.api.objectsWeb

import com.zdy.wallpaperinstallapp.api.objectsWeb.nekoImageObjects.NekoImage

data class RequestImages(
    val count: Int = 0,
    val items: List<NekoImage>,
)

