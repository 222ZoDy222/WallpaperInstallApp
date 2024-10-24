package com.zdy.wallpaperinstallapp.WallpapersList.RecycleView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.models.LocalSave.BitmapSaveManager
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

sealed class ImagesRecycleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    class WallpaperRecycleViewHolder(private val itemView: View,
                                     private val lifecycleScope: LifecycleCoroutineScope) :
        ImagesRecycleViewHolder(itemView) {

        private var currentURL : String? = null
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: ItemRecycle.RecycleWallpaperItem) {
            currentURL = item.image.url
            itemView.apply {


                if(item.image.bitmap == null){
                    if(item.image.isLiked){
                        val view = this
                        lifecycleScope.launch {
                            val bitmap = BitmapSaveManager.loadImageWallpaper(item.image,context)
                            withContext(Dispatchers.Main){
                                // if it's already not loaded element
                                if(currentURL == item.image.url){
                                    if(bitmap != null){
                                        item.image.bitmap = bitmap
                                        val imageView = findViewById<ImageView>(R.id.imageWallpaper)
                                        imageView.setImageBitmap(bitmap)
                                        loadImage(view, item, context)
                                    } else{
                                        loadImage(view, item, context)
                                    }
                                }

                            }
                        }
                    } else{
                        loadImage(this, item, context)
                    }
                }


                findViewById<TextView>(R.id.wallpaperDescription).text = item.image.description.toString()

                val imageID = if(item.image.isLiked) R.drawable.liked_icon else R.drawable.like_icon
                val iconLike: Drawable =
                    resources.getDrawable(imageID, context.theme);
                findViewById<ImageButton>(R.id.include_like_button).setImageDrawable(iconLike)



            }

        }

        // Loading image by URL from server
        private fun loadImage(view: View, item: ItemRecycle.RecycleWallpaperItem, context: Context?){

            val progressbar = view.findViewById<ProgressBar>(R.id.progress_bar)
            val imageView = view.findViewById<ImageView>(R.id.imageWallpaper)
            progressbar.visibility = View.VISIBLE
            currentURL?.let { url ->
                ImagesRepository.LoadBitmapByURL(view,
                    url,
                    object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            if(currentURL == item.image.url){
                                progressbar.visibility = View.GONE
                                item.image.bitmap = resource
                                if(item.image.isLiked){
                                    lifecycleScope.launch{
                                        BitmapSaveManager.saveImageWallpaper(item.image, context!!)
                                    }

                                }
                                imageView.setImageBitmap(resource)
                            }

                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {

                        }

                    })
            }

        }

    }

    class ButtonRecycleViewHolder(private val itemView: View) : ImagesRecycleViewHolder(itemView) {

    }

}