package com.zdy.wallpaperinstallapp.inheritance

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.zdy.wallpaperinstallapp.activity.webList.viewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.models.localSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.inheritance.recycleView.viewModel.RecycleViewModel
import com.zdy.wallpaperinstallapp.models.localSave.TransferBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

open class ListViewModel @Inject constructor(
    localSaveModel: LocalSaveModel
) : RecycleViewModel(localSaveModel) {

    private val imageToPickUp = MutableLiveData<PickUpImage>()
    fun getImageToPickUp() : MutableLiveData<PickUpImage> = imageToPickUp

    fun pickUpImage(pickImage: PickUpImage?, context: Context){

        if(pickImage != null){
            saveImage(pickImage, context)
        }
        imageToPickUp.value = pickImage
    }

    private fun saveImage(pickImage: PickUpImage, context: Context){
        pickImage.bitmap?.let {
            val transferBitmap = TransferBitmap()
            transferBitmap.saveBitmapToFile(it, context)
        }
    }

}