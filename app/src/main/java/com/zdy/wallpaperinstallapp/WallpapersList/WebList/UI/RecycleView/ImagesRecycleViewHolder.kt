package com.zdy.wallpaperinstallapp.WallpapersList.WebList.UI.RecycleView

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.DB.WallpaperDatabase
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class ImagesRecycleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    class WallpaperRecycleViewHolder(private val itemView: View) :
        ImagesRecycleViewHolder(itemView) {

        private var currentURL : String? = null
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: ItemRecycle.RecycleWallpaperItem) {
            currentURL = item.image.url
            itemView.apply {
                // Load image by URL
                val progressbar = findViewById<ProgressBar>(R.id.progress_bar)
                val imageView = findViewById<ImageView>(R.id.imageWallpaper)
                progressbar.visibility = View.VISIBLE

                currentURL?.let { url ->
                    ImagesRepository.LoadBitmapByURL(this,
                        url,
                        object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                if(currentURL == item.image.url){
                                    progressbar.visibility = View.GONE
                                    //item.bitmap = resource
                                    imageView.setImageBitmap(resource)
                                }

                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {

                            }

                        })
                }


                findViewById<TextView>(R.id.wallpaperDescription).text = item.image.description.toString()

                val imageID = if(item.image.isLiked) R.drawable.liked_icon else R.drawable.like_icon
                val iconLike: Drawable =
                    resources.getDrawable(imageID, context.theme);
                findViewById<ImageButton>(R.id.like_button).setImageDrawable(iconLike)



            }

        }
    }

    class ButtonRecycleViewHolder(private val itemView: View) : ImagesRecycleViewHolder(itemView) {

    }

}