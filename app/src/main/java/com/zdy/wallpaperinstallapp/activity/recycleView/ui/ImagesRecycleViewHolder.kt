package com.zdy.wallpaperinstallapp.activity.recycleView.ui

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.databinding.ItemImageLayoutBinding

sealed class ImagesRecycleViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {

    class WallpaperRecycleViewHolder(private val binding: ItemImageLayoutBinding) : ImagesRecycleViewHolder(binding) {

        private var currentURL : String? = null
        var onItemClickListener: ((PickUpImage)->Unit)? = null
        var onItemLikeClickListener: ((ItemRecycle.RecycleWallpaperItem)->Unit)? = null
        fun bind(item: ItemRecycle.RecycleWallpaperItem) {
            currentURL = item.image.url
            itemView.apply {
                if(item.image.bitmap != null){
                    binding.progressBar.visibility = View.GONE
                    binding.imageWallpaper.setImageBitmap(item.image.bitmap)
                } else{
                    binding.progressBar.visibility = View.VISIBLE
                }
                binding.wallpaperDescription.text = item.image.description.toString()
                val imageID = if(item.image.isLiked) R.drawable.liked_icon else R.drawable.like_icon
                val iconLike: Drawable? = ResourcesCompat.getDrawable(resources, imageID, context.theme)
                binding.includeLikeButton.likeButton.setImageDrawable(iconLike)

                setOnClickListener {
                    onItemClickListener?.invoke(item.image)
                }

                binding.includeLikeButton.likeButton.setOnClickListener {
                    onItemLikeClickListener?.invoke(item)
                }
            }
        }
    }

    class ButtonRecycleViewHolder(private val binding: ViewBinding) : ImagesRecycleViewHolder(binding) {
        var onRefreshClickListener: (()->Unit)? = null
        fun bind(item: ItemRecycle.RecycleButtonItem){
            binding.root.setOnClickListener {
                onRefreshClickListener?.invoke()
            }
        }

    }

}