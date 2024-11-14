package com.zdy.wallpaperinstallapp.api.objectsWeb.nekoImageObjects

data class Tag(
    val description: String,
    val id: Int,
    val id_v2: String,
    val is_nsfw: Boolean,
    val name: String,
    val sub: String
)