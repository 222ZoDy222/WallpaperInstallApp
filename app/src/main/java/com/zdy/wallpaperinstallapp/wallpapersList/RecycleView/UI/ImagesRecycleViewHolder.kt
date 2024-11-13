package com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.UI

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.zdy.wallpaperinstallapp.R

sealed class ImagesRecycleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    class WallpaperRecycleViewHolder(private val itemView: View) :
        ImagesRecycleViewHolder(itemView) {

        private var currentURL : String? = null

        fun bind(item: ItemRecycle.RecycleWallpaperItem) {
            currentURL = item.image.url
            itemView.apply {

                val progressbar = findViewById<ProgressBar>(R.id.progress_bar)

                if(item.image.bitmap != null){
                    val imageView = findViewById<ImageView>(R.id.imageWallpaper)
                    progressbar.visibility = View.GONE
                    imageView.setImageBitmap(item.image.bitmap)
                } else{
                    progressbar.visibility = View.VISIBLE
                }
                findViewById<TextView>(R.id.wallpaperDescription).text = item.image.description.toString()
                val imageID = if(item.image.isLiked) R.drawable.liked_icon else R.drawable.like_icon
                val iconLike: Drawable? = ResourcesCompat.getDrawable(resources, imageID, context.theme)
                //resources.getDrawable(imageID, context.theme);
                findViewById<ImageButton>(R.id.include_like_button).setImageDrawable(iconLike)

            }

        }


    }

    class ButtonRecycleViewHolder(private val itemView: View) : ImagesRecycleViewHolder(itemView) {

    }

}