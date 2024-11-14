package com.zdy.wallpaperinstallapp.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.zdy.wallpaperinstallapp.db.WallpaperDatabase
import com.zdy.wallpaperinstallapp.db.objectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.api.ImagesAPI


class ImagesRepository(
    val db: WallpaperDatabase,
    val api: ImagesAPI
) {

    suspend fun getRandomImages(limit: Int = 31) = api.GetRandomImages(limit.toString())

    suspend fun insert(wallpaper: LocalWallpaper) = db.getWallpaperDao().insert(wallpaper)

    fun getSavedWallpaper() = db.getWallpaperDao().getSavedWallpapers()

    suspend fun delete(wallpaper: LocalWallpaper) = db.getWallpaperDao().delete(wallpaper)

    suspend fun deleteByUrl(url: String) = db.getWallpaperDao().deleteByURL(url)

    suspend fun alreadyHave(url: String): Boolean{
        val wallpapers = db.getWallpaperDao().AlreadySaved(url)
        return wallpapers.isNotEmpty()
    }

}