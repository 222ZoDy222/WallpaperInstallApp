package com.zdy.wallpaperinstallapp.WallpapersList.UI.RecycleView

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.bumptech.glide.request.target.Target
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.Web.Objects.NekoImage


class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<NekoImage>(){
        override fun areItemsTheSame(oldItem: NekoImage, newItem: NekoImage): Boolean = oldItem.image_url == newItem.image_url
        override fun areContentsTheSame(oldItem: NekoImage, newItem: NekoImage): Boolean = oldItem == newItem
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
            val progressbar = findViewById<ProgressBar>(R.id.progress_bar)
            val imageView = findViewById<ImageView>(R.id.imageWallpaper)
            progressbar.visibility = View.VISIBLE
            Glide.with(this)
                .load(item.image_url)
                .addListener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressbar.visibility = View.GONE
                        imageView.setImageDrawable(resource)
                        return false
                    }

                })
                .into(findViewById(R.id.imageWallpaper))
            try{
                findViewById<TextView>(R.id.wallpaperDescription).text = item.tags.toString()
            } catch (ex: Exception){
                findViewById<TextView>(R.id.wallpaperDescription).text = "Some image harcored"
            }

            setOnClickListener{
                onItemClickListener?.invoke(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((NekoImage)->Unit)? = null


    fun setOnItemClickListener(listener : (NekoImage) -> Unit){
        onItemClickListener = listener
    }

}