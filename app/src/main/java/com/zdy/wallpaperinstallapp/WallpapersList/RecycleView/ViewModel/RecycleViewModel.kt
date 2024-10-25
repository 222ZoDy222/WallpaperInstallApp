package com.zdy.wallpaperinstallapp.WallpapersList.RecycleView.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.WallpapersList.LikedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.RecycleView.Model.RecycleConverter
import com.zdy.wallpaperinstallapp.WallpapersList.RecycleView.UI.ItemRecycle
import com.zdy.wallpaperinstallapp.models.LocalSave.BitmapSaveManager
import com.zdy.wallpaperinstallapp.models.ObjectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import com.zdy.wallpaperinstallapp.models.Web.ObjectsWeb.RequestImages
import kotlinx.coroutines.launch

class RecycleViewModel(
    private val application: Application,
    private val likedListViewModel: WallpaperLikedListViewModel,
) : AndroidViewModel(application) {



    private val itemsRecycle : MutableLiveData<List<ItemRecycle>> = MutableLiveData(null)

    private var onUpdateItem: ((ItemRecycle)->Unit)? = null

    fun setOnUpdateItem(listener : (ItemRecycle) -> Unit){
        onUpdateItem = listener
    }

    fun getItemsRecycle() = itemsRecycle

    fun setWebList(requestImages: RequestImages){
        itemsRecycle.value = null
        val newList = RecycleConverter.ConvertImages(requestImages)
        val listItems = mutableListOf<ItemRecycle>()

        for(i in newList){
            listItems.add(ItemRecycle.RecycleWallpaperItem(i))
        }

        listItems.add(ItemRecycle.RecycleButtonItem())

        itemsRecycle.value = listItems


        loadImages()

    }

    fun setLocalList(images: List<LocalWallpaper>){
        itemsRecycle.value = null
        val listItems = RecycleConverter.ConvertImages(images)
        itemsRecycle.value = listItems
        loadImages()
    }

    fun onLikeImage(item: ItemRecycle.RecycleWallpaperItem){
        likedListViewModel.onLikeClicked(item.image)
        onUpdateItem?.invoke(item)

    }

    // Load local or Web images
    private fun loadImages(){

        viewModelScope.launch {
            itemsRecycle.value?.forEach itemScope@{item->
                if(item !is ItemRecycle.RecycleWallpaperItem) return@itemScope
                val wallpaperItem = item as ItemRecycle.RecycleWallpaperItem
                // try to get saved bitmap
                val bitmap = BitmapSaveManager.loadImageWallpaper(wallpaperItem.image, application.applicationContext)

                // it's already exist image, so it's also liked image
                if(bitmap != null){
                    item.image.isLiked = true
                    item.image.bitmap = bitmap
                    onUpdateItem?.invoke(item)
                    return@itemScope
                } else{
                    loadWeb(item)
                    return@itemScope
                }




            }
        }

    }

    private suspend fun loadWeb(item : ItemRecycle.RecycleWallpaperItem) {
        item.image.url?.let { url->
            ImagesRepository.LoadBitmapByURL(application.applicationContext,url,
                object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        item.image.bitmap = resource
                        if(item.image.isLiked){
                            viewModelScope.launch {
                                BitmapSaveManager.saveImageWallpaper(item.image,application.applicationContext)
                            }
                        }
                        onUpdateItem?.invoke(item)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                })
        }
    }


}