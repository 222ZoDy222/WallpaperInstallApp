package com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdy.wallpaperinstallapp.Web.Objects.NekoImage

class PickUpWallpaperViewModel : ViewModel() {


    val imageToPickUp = MutableLiveData<NekoImage>()
    //fun getImageToPickUp() : MutableLiveData<NekoImage> = imageToPickUp

    fun PickUpImage(nekoImage: NekoImage){
        imageToPickUp.value = nekoImage
    }



}