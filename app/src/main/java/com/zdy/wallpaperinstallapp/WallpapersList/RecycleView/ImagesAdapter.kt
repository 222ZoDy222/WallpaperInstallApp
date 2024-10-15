package com.zdy.wallpaperinstallapp.WallpapersList.RecycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.Web.Objects.nekoImage


class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<nekoImage>(){
        override fun areItemsTheSame(oldItem: nekoImage, newItem: nekoImage): Boolean = oldItem.image_url == newItem.image_url
        override fun areContentsTheSame(oldItem: nekoImage, newItem: nekoImage): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_image_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        var item = differ.currentList[position]
        holder.itemView.apply {
            // Load image by URL
            Glide.with(this).load(item.image_url).into(findViewById(R.id.imageWallpaper))
            findViewById<TextView>(R.id.wallpaperDescription).text = item.tags.toString()

            setOnItemClickListener {
                onItemClickListener?.invoke(item)
            }

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((nekoImage)->Unit)? = null


    fun setOnItemClickListener(listener : (nekoImage) -> Unit){
        onItemClickListener = listener
    }

}