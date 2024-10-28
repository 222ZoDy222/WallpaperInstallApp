package com.zdy.wallpaperinstallapp.models.ObjectsDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "WallpaperImages"
)
data class LocalWallpaper(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val description: String,
    val image_url: String,
)
