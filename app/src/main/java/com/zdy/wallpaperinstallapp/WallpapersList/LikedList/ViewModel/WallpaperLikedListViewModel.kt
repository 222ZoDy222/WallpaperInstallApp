package com.zdy.wallpaperinstallapp.WallpapersList.LikedList.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.models.ObjectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import kotlinx.coroutines.launch

class WallpaperLikedListViewModel(
    private val application: Application,
    val imageRepository: ImagesRepository
) : AndroidViewModel(application) {



    fun onLikeClicked(wallpaper: PickUpImage) : String{

        if(wallpaper.isLiked){
            // TODO: Delete
            return "Wallpaper deleted from liked"
        } else{
            return saveWallpaper(wallpaper)
        }

    }
    private fun saveWallpaper(wallpaper: PickUpImage) : String{
        if(wallpaper.url != null && wallpaper.description != null){
            val localWallpaper = LocalWallpaper(
                description = wallpaper.description,
                image_url =  wallpaper.url,
                image_path = "",
            )
            // TODO: Save Image in local path
            saveWallpaper(localWallpaper)
            wallpaper.isLiked = true
            return "Wallpaper saved"
        }
        return "Wallpaper save ERROR"

    }
    private fun saveWallpaper(wallpaper: LocalWallpaper) = viewModelScope.launch {
        imageRepository.insert(wallpaper)
    }

    fun getSavedWallpaper() = imageRepository.getSavedWallpaper()

    fun deleteWallpaper(wallpaper: LocalWallpaper) = viewModelScope.launch {
        imageRepository.delete(wallpaper)
    }

    fun alreadyHaveWallpaper(url: String) : LiveData<Boolean>{
        val result = MutableLiveData<Boolean>()

        viewModelScope.launch {

            result.value = imageRepository.alreadyHave(url)
        }

        return result


    }

}