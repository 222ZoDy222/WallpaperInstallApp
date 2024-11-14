package com.zdy.wallpaperinstallapp.inheritance.recycleView.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.models.recycleModels.RecycleConverter
import com.zdy.wallpaperinstallapp.inheritance.recycleView.ui.ItemRecycle
import com.zdy.wallpaperinstallapp.models.localSave.BitmapSaveManager
import com.zdy.wallpaperinstallapp.models.localSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.db.objectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.repository.ImagesRepository
import com.zdy.wallpaperinstallapp.api.objectsWeb.RequestImages
import com.zdy.wallpaperinstallapp.models.web.GlideModel
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
        val wallpaperItem = item
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
            GlideModel.LoadBitmapByURL(context,url,
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