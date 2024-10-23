package com.zdy.wallpaperinstallapp.models.Web.ObjectsWeb

import com.zdy.wallpaperinstallapp.models.Web.ObjectsWeb.CustomServerObjects.WebImage

data class RequestImages(
    val count: Int = 0,
    val items: List<WebImage>,
)

