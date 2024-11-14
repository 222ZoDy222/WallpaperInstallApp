package com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.activity.webList.viewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.models.localSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.models.localSave.TransferBitmap
import com.zdy.wallpaperinstallapp.models.web.GlideModel
import com.zdy.wallpaperinstallapp.repository.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PickUpWallpaperViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository
) : SetWallpaperViewModel() {

    private val localSaveModel: LocalSaveModel = LocalSaveModel(imagesRepository)

    var selectedImage : MutableLiveData<PickUpImage> = MutableLiveData()

    private var backgroundDrawable : MutableLiveData<Drawable> = MutableLiveData()
    fun getBackgroundDrawable() = backgroundDrawable


    fun onLikeImage(context: Context){
        selectedImage.value?.let {
            viewModelScope.launch {
                localSaveModel.onLikeClicked(selectedImage.value!!, context)
                selectedImage.value = selectedImage.value
            }
        }
    }

    fun selectImage(image : PickUpImage, context: Context){
        val transferBitmap = TransferBitmap()

        val bitmap = transferBitmap.loadBitmapFromFile(context)
        bitmap?.let {
            image.bitmap = it
        }
        transferBitmap.deleteSavedImage(context)
        selectedImage.value = image

        setBackgroundImage(context)
    }


    private fun loadWeb(image: PickUpImage, context: Context){
        GlideModel.LoadBitmapByURL(context,image.url, object : CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                selectedImage.value?.bitmap = resource
                backgroundDrawable.value = resource.toDrawable(context.resources)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}

            override fun onLoadFailed(errorDrawable: Drawable?) {
                loadWeb(image, context)
            }
        })
    }

    private fun setBackgroundImage(context: Context){

        if(selectedImage.value != null){
            if(selectedImage.value!!.bitmap != null){

                val drawable = BitmapDrawable(
                    context.resources,
                    selectedImage.value!!.bitmap)

                backgroundDrawable.value = drawable

            } else{
                loadWeb(selectedImage.value!!, context)

            }
        }
    }


    // If user clicked so fast ui must update and resize
    fun updateDrawableImage(){
        if(backgroundDrawable.value != null){
            backgroundDrawable.value = backgroundDrawable.value
        }
    }






}