package com.zdy.wallpaperinstallapp.activity.recycleView.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.databinding.ItemButtonLayoutBinding
import com.zdy.wallpaperinstallapp.databinding.ItemImageLayoutBinding
import java.lang.IllegalArgumentException


class ImagesAdapter() : RecyclerView.Adapter<ImagesRecycleViewHolder>() {


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
                    ItemImageLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )


            }
            R.layout.item_button_layout ->{
                ImagesRecycleViewHolder.ButtonRecycleViewHolder(
                    ItemButtonLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw throw  IllegalArgumentException("Invalid view type provider")
        }

    }


    override fun onBindViewHolder(holder: ImagesRecycleViewHolder, position: Int) {
        val item = differ.currentList[position]

        when(holder){
            is ImagesRecycleViewHolder.ButtonRecycleViewHolder -> {
                holder.onRefreshClickListener = onRefreshClickListener
                holder.bind(item as ItemRecycle.RecycleButtonItem)

            }
            is ImagesRecycleViewHolder.WallpaperRecycleViewHolder -> {
                holder.onItemLikeClickListener = onItemLikeClickListener
                holder.onItemClickListener = onItemClickListener
                holder.bind(item as ItemRecycle.RecycleWallpaperItem)
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

    fun isButtonType(position: Int) : Boolean{
        return when(differ.currentList[position]){
            is ItemRecycle.RecycleButtonItem -> true
            is ItemRecycle.RecycleWallpaperItem -> false
        }
    }

    private var onItemClickListener: ((PickUpImage)->Unit)? = null

    private var onItemLikeClickListener: ((ItemRecycle.RecycleWallpaperItem)->Unit)? = null

    private var onRefreshClickListener: (()->Unit)? = null

    fun setOnItemClickListener(listener : (PickUpImage) -> Unit){
        onItemClickListener = listener
    }

    fun setOnItemLikeClickListener(listener : (ItemRecycle.RecycleWallpaperItem) -> Unit){
        onItemLikeClickListener = listener
    }

    fun setOnRefreshClickListener(listener: () -> Unit){
        onRefreshClickListener = listener
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

    fun updateImage(item: ItemRecycle.RecycleWallpaperItem){
        val index = differ.currentList.indexOf(item)
        notifyItemChanged(index)
    }


}