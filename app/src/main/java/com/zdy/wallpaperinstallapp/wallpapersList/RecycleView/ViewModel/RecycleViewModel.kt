package com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.ViewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.activity.likedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.Model.RecycleConverter
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.UI.ItemRecycle
import com.zdy.wallpaperinstallapp.models.LocalSave.BitmapSaveManager
import com.zdy.wallpaperinstallapp.models.LocalSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.models.ObjectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import com.zdy.wallpaperinstallapp.models.Web.ObjectsWeb.RequestImages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class RecycleViewModel @Inject constructor(
    private val localSaveModel: LocalSaveModel,
) : ViewModel() {


    private val itemsRecycle : MutableLiveData<List<ItemRecycle>> = MutableLiveData(null)

    private var onUpdateItem: ((ItemRecycle)->Unit)? = null

    fun setOnUpdateItem(listener : (ItemRecycle) -> Unit){
        onUpdateItem = listener
    }

    fun getItemsRecycle() = itemsRecycle

    fun setWebList(requestImages: RequestImages, context: Context){
        itemsRecycle.value = null
        val newList = RecycleConverter.ConvertImages(requestImages)
        val listItems = mutableListOf<ItemRecycle>()

        for(i in newList){
            listItems.add(ItemRecycle.RecycleWallpaperItem(i))
        }

        listItems.add(ItemRecycle.RecycleButtonItem())

        itemsRecycle.value = listItems


        loadImages(context)

    }

    fun setLocalList(images: List<LocalWallpaper>, context: Context){
        itemsRecycle.value = null
        val listItems = RecycleConverter.ConvertImages(images)
        itemsRecycle.value = listItems
        loadImages(context)
    }

    fun onLikeImage(item: ItemRecycle.RecycleWallpaperItem, context: Context){
        //TODO: Refactor
        viewModelScope.launch {
            localSaveModel.onLikeClicked(item.image, context)
            onUpdateItem?.invoke(item)
        }

    }


    fun checkForUpdates(image: PickUpImage){
        viewModelScope.launch{
            itemsRecycle.value?.forEach itemScope@{item->
                if(item is ItemRecycle.RecycleWallpaperItem){
                    if(item.image.url == image.url){
                        item.image.isLiked = image.isLiked
                        onUpdateItem?.invoke(item)
                    }
                }
            }
        }
    }

    // Load local or Web images
    private fun loadImages(context: Context){

        viewModelScope.launch {
            itemsRecycle.value?.forEach itemScope@{item->
                loadImage(item, context)
            }
        }
    }
    private suspend fun loadImage(item: ItemRecycle, context: Context){
        if(item !is ItemRecycle.RecycleWallpaperItem) return
        loadImage(item, context)
    }
    private suspend fun loadImage(item: ItemRecycle.RecycleWallpaperItem, context: Context){
        val wallpaperItem = item as ItemRecycle.RecycleWallpaperItem
        // try to get saved bitmap
        val bitmap = BitmapSaveManager.loadImageWallpaper(wallpaperItem.image, context)

        // it's already exist image, so it's also liked image
        if(bitmap != null){
            item.image.isLiked = true
            item.image.bitmap = bitmap
            onUpdateItem?.invoke(item)
            return
        } else{
            loadWeb(item, context)
            return
        }
    }

    private suspend fun loadWeb(item : ItemRecycle.RecycleWallpaperItem, context: Context) {
        item.image.url.let { url->
            ImagesRepository.LoadBitmapByURL(context,url,
                object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        item.image.bitmap = resource
                        if(item.image.isLiked){
                            viewModelScope.launch {
                                BitmapSaveManager.saveImageWallpaper(item.image,context)
                            }
                        }
                        onUpdateItem?.invoke(item)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        viewModelScope.launch {
                            loadImage(item, context)
                        }
                    }

                })
        }
    }


}