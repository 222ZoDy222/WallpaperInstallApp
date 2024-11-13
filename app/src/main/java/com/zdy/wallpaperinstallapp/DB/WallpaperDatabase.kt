package com.zdy.wallpaperinstallapp.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zdy.wallpaperinstallapp.models.ObjectsDB.LocalWallpaper


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