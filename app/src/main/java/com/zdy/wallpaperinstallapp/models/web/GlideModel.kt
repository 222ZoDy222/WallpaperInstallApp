package com.zdy.wallpaperinstallapp.models.web

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

class GlideModel {

    companion object{

        fun loadBitmapByURL(context: Context, url : String, target : CustomTarget<Bitmap>){
            Glide.with(context)
                .asBitmap()
                .load(url)
                .into(target)
        }

    }

}