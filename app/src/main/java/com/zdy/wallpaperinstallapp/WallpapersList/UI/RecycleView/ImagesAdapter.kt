package com.zdy.wallpaperinstallapp.WallpapersList.UI.RecycleView

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.PickUpWallpaper.Objects.PickUpImage
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.Web.Objects.NekoImage
import com.zdy.wallpaperinstallapp.Web.Repository.ImagesRepository
import com.zdy.wallpaperinstallapp.Web.Requests.ImageRepository
import java.lang.IllegalArgumentException


class ImagesAdapter : RecyclerView.Adapter<ImagesRecycleViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<ItemRecycle>(){
        override fun areItemsTheSame(oldItem: ItemRecycle, newItem: ItemRecycle): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: ItemRecycle, newItem: ItemRecycle): Boolean{
            return if(oldItem is ItemRecycle.RecycleWallpaperItem
                && newItem is ItemRecycle.RecycleWallpaperItem){
                (oldItem).image.url == (newItem).image.url
            } else false
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesRecycleViewHolder {

        return when(viewType){
            R.layout.item_image_layout ->{
                ImagesRecycleViewHolder.WallpaperRecycleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_image_layout,
                        parent,
                        false
                    )
                )
            }
            R.layout.item_button_layout ->{
                ImagesRecycleViewHolder.ButtonRecycleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_button_layout,
                        parent,
                        false
                    )
                )
            }
            else -> throw throw  IllegalArgumentException("Invalid view type provider")
        }

    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ImagesRecycleViewHolder, position: Int) {
        var item = differ.currentList[position]

        when(holder){
            is ImagesRecycleViewHolder.ButtonRecycleViewHolder -> {
                holder.itemView.apply {
                    findViewById<ImageButton>(R.id.refresh_button).setOnClickListener {
                        onRefreshClickListener?.invoke()
                    }
                }
            }
            is ImagesRecycleViewHolder.WallpaperRecycleViewHolder -> {
                holder.bind(item as ItemRecycle.RecycleWallpaperItem)
                holder.itemView.apply {
                    setOnClickListener{
                        onItemClickListener?.invoke(item.image)
                    }
                }
            }
        }



    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(differ.currentList[position]){
            is ItemRecycle.RecycleButtonItem -> R.layout.item_button_layout
            is ItemRecycle.RecycleWallpaperItem -> R.layout.item_image_layout
        }
    }

    private var onItemClickListener: ((PickUpImage)->Unit)? = null

    private var onRefreshClickListener: (()->Unit)? = null

    fun setOnItemClickListener(listener : (PickUpImage) -> Unit){
        onItemClickListener = listener
    }

    fun setOnRefreshClickListener(listener: () -> Unit){
        onRefreshClickListener = listener
    }

}