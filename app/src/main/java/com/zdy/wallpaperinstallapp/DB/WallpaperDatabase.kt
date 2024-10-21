package com.zdy.wallpaperinstallapp.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zdy.wallpaperinstallapp.models.ObjectsWeb.NekoImage

@Database(
    entities = [NekoImage::class],
    version = 1
)
abstract class WallpaperDatabase : RoomDatabase() {

    abstract fun getWallpaperDao() : WallpaperImageDao

    companion object{
        @Volatile
        private var instance: WallpaperDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WallpaperDatabase::class.java,
                DATABASE_NAME
            ).build()

        private const val DATABASE_NAME = "wallpaper_db.db"
    }

}