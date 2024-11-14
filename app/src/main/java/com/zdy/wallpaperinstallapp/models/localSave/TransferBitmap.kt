package com.zdy.wallpaperinstallapp.models.localSave

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.zdy.wallpaperinstallapp.activity.webList.viewModel.WallpaperListViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class TransferBitmap {

    fun deleteSavedImage(context: Context){
        val file = File(context.cacheDir, SELECTED_IMAGE_NAME)
        if (file.exists()) {
            file.delete()
        }
    }

    fun loadBitmapFromFile(context: Context): Bitmap? {
        val file = File(context.cacheDir, SELECTED_IMAGE_NAME)
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    fun saveBitmapToFile(bitmap: Bitmap, context: Context): String? {
        try {
            val file = File(context.cacheDir, SELECTED_IMAGE_NAME)
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

    companion object{

        const val SELECTED_IMAGE_NAME = "SelectedImage.wi"


    }


}