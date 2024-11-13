package com.zdy.wallpaperinstallapp.inheritance

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdy.wallpaperinstallapp.activity.webList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.models.LocalSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.ViewModel.RecycleViewModel
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
            saveBitmapToFile(it, context)
        }

    }

    private fun saveBitmapToFile(bitmap: Bitmap, context: Context): String? {
        try {
            val file = File(context.cacheDir, WallpaperListViewModel.SELECTED_IMAGE_NAME)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


}