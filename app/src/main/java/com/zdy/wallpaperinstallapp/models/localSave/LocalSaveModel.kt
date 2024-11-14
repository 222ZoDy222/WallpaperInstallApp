package com.zdy.wallpaperinstallapp.models.localSave

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.zdy.wallpaperinstallapp.db.objectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.repository.ImagesRepository

class LocalSaveModel(
    private val imagesRepository: ImagesRepository
) {


    suspend fun onLikeClicked(wallpaper: PickUpImage, context: Context) : String{

        return if(wallpaper.isLiked){
            deleteWallpaper(wallpaper, context)
        } else{
            saveWallpaper(wallpaper, context)
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