package com.zdy.wallpaperinstallapp.models.Repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.DB.WallpaperDatabase
import com.zdy.wallpaperinstallapp.models.Web.RetrofitInstance
import com.zdy.wallpaperinstallapp.models.ObjectsDB.LocalWallpaper

class ImagesRepository(
    val db: WallpaperDatabase
) {

    suspend fun getRandomImages(limit: Int = 30) = RetrofitInstance.api.GetRandomImages(limit.toString())

    suspend fun insert(wallpaper: LocalWallpaper) = db.getWallpaperDao().insert(wallpaper)

    fun getSavedWallpaper() = db.getWallpaperDao().getSavedWallpapers()

    suspend fun delete(wallpaper: LocalWallpaper) = db.getWallpaperDao().delete(wallpaper)

    companion object
    {
        fun LoadBitmapByURL(view : View, url : String,target : CustomTarget<Bitmap>){
            LoadBitmapByURL(view.context,url,target)
        }


        fun LoadBitmapByURL(context: Context, url : String, target : CustomTarget<Bitmap>){

            Glide.with(context)
                .asBitmap()
                .load(url)
                .into(target)

        }


    }


}