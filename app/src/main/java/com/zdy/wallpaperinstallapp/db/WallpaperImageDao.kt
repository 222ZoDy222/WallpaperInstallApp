package com.zdy.wallpaperinstallapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zdy.wallpaperinstallapp.db.objectsDB.LocalWallpaper



@Dao
interface WallpaperImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallpaper: LocalWallpaper) : Long

    @Query(
        "SELECT * FROM WallpaperImages"
    )
    fun getSavedWallpapers() : LiveData<List<LocalWallpaper>>


    @Delete
    suspend fun delete(wallpaper: LocalWallpaper)

    @Query(
        "DELETE FROM WallpaperImages WHERE image_url = :url"
    )
    suspend fun deleteByURL(url: String)


    @Query(
        "SELECT * FROM WallpaperImages WHERE image_url=:url"
    )
    suspend fun AlreadySaved(url: String) : List<LocalWallpaper>



}