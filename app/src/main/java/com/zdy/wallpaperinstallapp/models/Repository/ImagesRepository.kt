package com.zdy.wallpaperinstallapp.models.Repository

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
import com.bumptech.glide.request.target.CustomTarget
import com.zdy.wallpaperinstallapp.DB.WallpaperDatabase
import com.zdy.wallpaperinstallapp.models.Web.RetrofitInstance
import com.zdy.wallpaperinstallapp.models.ObjectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.models.Web.SSL.GlideApp

class ImagesRepository(
    val db: WallpaperDatabase
) {

    suspend fun getRandomImages(limit: Int = 31) = RetrofitInstance.api.GetRandomImages(limit.toString())

    suspend fun insert(wallpaper: LocalWallpaper) = db.getWallpaperDao().insert(wallpaper)

    fun getSavedWallpaper() = db.getWallpaperDao().getSavedWallpapers()

    suspend fun delete(wallpaper: LocalWallpaper) = db.getWallpaperDao().delete(wallpaper)

    suspend fun deleteByUrl(url: String) = db.getWallpaperDao().deleteByURL(url)

    suspend fun alreadyHave(url: String): Boolean{
        val wallpapers = db.getWallpaperDao().AlreadySaved(url)
        return wallpapers.isNotEmpty()
    }


    companion object
    {
        fun LoadBitmapByURL(context: Context, url : String, target : CustomTarget<Bitmap>){
            GlideApp.with(context)
                .asBitmap()
                .load(url)
                .into(target)
        }

        fun hasInternetConnection(context: Context): Boolean {
            val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
                return when {
                    capabilities.hasTransport(TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {

                @Suppress("DEPRECATION")
                connectivityManager.activeNetworkInfo?.run {
                    return when(type) {
                        TYPE_WIFI -> true
                        TYPE_MOBILE -> true
                        TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
            return false
        }

    }


}