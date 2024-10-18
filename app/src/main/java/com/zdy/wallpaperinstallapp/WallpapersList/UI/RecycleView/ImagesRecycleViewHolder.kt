package com.zdy.wallpaperinstallapp.WallpapersList.UI.RecycleView

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.Web.Repository.ImagesRepository

sealed class ImagesRecycleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    class WallpaperRecycleViewHolder(private val itemView: View) :
        ImagesRecycleViewHolder(itemView) {
        fun bind(item: ItemRecycle.RecycleWallpaperItem) {

            itemView.apply {
                // Load image by URL
                val progressbar = findViewById<ProgressBar>(R.id.progress_bar)
                val imageView = findViewById<ImageView>(R.id.imageWallpaper)
                progressbar.visibility = View.VISIBLE

                item.image.url?.let { url ->
                    ImagesRepository.LoadBitmapByURL(this,
                        url,
                        object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                progressbar.visibility = View.GONE
                                //item.bitmap = resource
                                imageView.setImageBitmap(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                // TODO: Error message
                                progressbar.visibility = View.GONE
                            }

                        })
                }

                findViewById<TextView>(R.id.wallpaperDescription).text = item.image.description.toString()

            }



        }




    }

    class ButtonRecycleViewHolder(private val itemView: View) : ImagesRecycleViewHolder(itemView) {

    }

}