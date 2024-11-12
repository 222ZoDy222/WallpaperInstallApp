package com.zdy.wallpaperinstallapp.models.LocalSave

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.models.ObjectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import kotlinx.coroutines.launch

class LocalSaveModel(
    private val imagesRepository: ImagesRepository
) {


    suspend fun onLikeClicked(wallpaper: PickUpImage, context: Context) : String{

        if(wallpaper.isLiked){
            return deleteWallpaper(wallpaper, context)
        } else{
            return saveWallpaper(wallpaper, context)
        }

    }

    private suspend fun saveWallpaper(wallpaper: PickUpImage, context: Context) : String{
        if(wallpaper.description != null){
            val localWallpaper = LocalWallpaper(
                id = wallpaper.localID,
                description = wallpaper.description,
                image_url =  wallpaper.url,
            )

            saveWallpaper(localWallpaper)

            BitmapSaveManager.saveImageWallpaper(wallpaper, context)

            wallpaper.isLiked = true
            return "Wallpaper saved"
        }
        return "Wallpaper save ERROR"
    }

    private suspend fun deleteWallpaper(wallpaper: PickUpImage, context: Context): String{
        if(wallpaper.description != null){
            val localWallpaper = LocalWallpaper(
                id = wallpaper.localID,
                description = wallpaper.description,
                image_url =  wallpaper.url,
            )
            deleteWallpaper(localWallpaper)

            BitmapSaveManager.deleteImageWallpaper(wallpaper, context)

            wallpaper.isLiked = false
            return "Wallpaper deleted"
        }
        return "Wallpaper delete ERROR"
    }

    private suspend fun saveWallpaper(wallpaper: LocalWallpaper) {
        imagesRepository.insert(wallpaper)
    }

    suspend fun deleteWallpaper(wallpaper: LocalWallpaper) {
        if(wallpaper.id != null)
            imagesRepository.delete(wallpaper)
        else imagesRepository.deleteByUrl(wallpaper.image_url)
    }

    suspend fun alreadyHaveWallpaper(url: String) : MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        result.value = imagesRepository.alreadyHave(url)
        return result
    }


}