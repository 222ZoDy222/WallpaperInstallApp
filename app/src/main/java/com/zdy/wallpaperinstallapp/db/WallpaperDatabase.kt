package com.zdy.wallpaperinstallapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zdy.wallpaperinstallapp.db.objectsDB.LocalWallpaper


@Database(
    entities = [LocalWallpaper::class],
    version = 1,
    exportSchema = false
)
abstract class WallpaperDatabase : RoomDatabase() {

    abstract fun getWallpaperDao() : WallpaperImageDao

    companion object{
        const val DATABASE_NAME = "wallpaper_db.db"
    }

}