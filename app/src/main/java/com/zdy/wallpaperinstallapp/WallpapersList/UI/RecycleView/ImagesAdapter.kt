package com.zdy.wallpaperinstallapp.WallpapersList.UI.RecycleView

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.PickUpWallpaper.Objects.PickUpImage
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.Web.Objects.NekoImage
import com.zdy.wallpaperinstallapp.Web.Repository.ImagesRepository
import com.zdy.wallpaperinstallapp.Web.Requests.ImageRepository


class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<PickUpImage>(){
        override fun areItemsTheSame(oldItem: PickUpImage, newItem: PickUpImage): Boolean = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: PickUpImage, newItem: PickUpImage): Boolean = oldItem == newItem
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

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        var item = differ.currentList[position]

        holder.itemView.apply {
            // Load image by URL
            val progressbar = findViewById<ProgressBar>(R.id.progress_bar)
            val imageView = findViewById<ImageView>(R.id.imageWallpaper)
            progressbar.visibility = View.VISIBLE
            item.url?.let {url ->
                ImagesRepository.LoadBitmapByURL(this,
                    url,
                    object : CustomTarget<Bitmap>(){
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



            findViewById<TextView>(R.id.wallpaperDescription).text = item.description.toString()

            setOnClickListener{
                onItemClickListener?.invoke(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((PickUpImage)->Unit)? = null


    fun setOnItemClickListener(listener : (PickUpImage) -> Unit){
        onItemClickListener = listener
    }

}