package com.zdy.wallpaperinstallapp.inheritance

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdy.wallpaperinstallapp.activity.webList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

open class ListViewModel : ViewModel() {


    private val imageToPickUp = MutableLiveData<PickUpImage>()
    fun getImageToPickUp() : MutableLiveData<PickUpImage> = imageToPickUp

    fun PickUpImage(pickImage: PickUpImage?, context: Context){
        if(pickImage != null){
            SaveImage(pickImage, context)
        }
        imageToPickUp.value = pickImage
    }

    private fun SaveImage(pickImage: PickUpImage, context: Context){

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