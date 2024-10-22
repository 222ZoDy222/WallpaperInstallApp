package com.zdy.wallpaperinstallapp.WallpapersList.WebList.UI.RecycleView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.R
import java.lang.IllegalArgumentException


class ImagesAdapter : RecyclerView.Adapter<ImagesRecycleViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<ItemRecycle>(){
        override fun areItemsTheSame(oldItem: ItemRecycle, newItem: ItemRecycle): Boolean {
            return if(oldItem is ItemRecycle.RecycleWallpaperItem
                && newItem is ItemRecycle.RecycleWallpaperItem
            ){
                (oldItem).image.url == (newItem).image.url
            } else false

        }
        override fun areContentsTheSame(oldItem: ItemRecycle, newItem: ItemRecycle): Boolean{
            return oldItem == newItem
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
                    findViewById<ImageButton>(R.id.like_button).setOnClickListener {
                        onItemLikeClickListener?.invoke(item.image)
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

    private var onItemLikeClickListener: ((PickUpImage)->Unit)? = null

    private var onRefreshClickListener: (()->Unit)? = null

    fun setOnItemClickListener(listener : (PickUpImage) -> Unit){
        onItemClickListener = listener
    }

    fun setOnItemLikeClickListener(listener : (PickUpImage) -> Unit){
        onItemLikeClickListener = listener
    }

    fun setOnRefreshClickListener(listener: () -> Unit){
        onRefreshClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateImageSavedStatus(itemRecycle: ItemRecycle.RecycleWallpaperItem, saved: Boolean?) {
        itemRecycle.image.isLiked = saved == true
        val indexItem = differ.currentList.indexOf(itemRecycle)
        notifyDataSetChanged()
    }

    fun updateImageSavedStatus(image: PickUpImage){
        for (wallpaperIndex in differ.currentList.indices){
            val wallpaper = differ.currentList[wallpaperIndex]
            if(wallpaper is ItemRecycle.RecycleWallpaperItem){
                if(wallpaper.image == image){
                    notifyItemChanged(wallpaperIndex)
                    break
                }
            }
        }
    }


}