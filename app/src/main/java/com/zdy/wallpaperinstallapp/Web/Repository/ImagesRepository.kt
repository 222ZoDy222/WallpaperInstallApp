package com.zdy.wallpaperinstallapp.Web.Repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.Web.RetrofitInstance

class ImagesRepository {


    suspend fun getRandomImages() = RetrofitInstance.api.GetRandomImages()

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