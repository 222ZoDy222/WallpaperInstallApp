package com.zdy.wallpaperinstallapp.WallpapersList.LikedList.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.WallpapersList.RecycleView.UI.ItemRecycle
import com.zdy.wallpaperinstallapp.models.LocalSave.BitmapSaveManager
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
            return deleteWallpaper(wallpaper)
        } else{
            return saveWallpaper(wallpaper)
        }

    }
    private fun saveWallpaper(wallpaper: PickUpImage) : String{
        if(wallpaper.description != null){
            val localWallpaper = LocalWallpaper(
                id = wallpaper.localID,
                description = wallpaper.description,
                image_url =  wallpaper.url,
            )

            saveWallpaper(localWallpaper)
            viewModelScope.launch {
                BitmapSaveManager.saveImageWallpaper(wallpaper, application.applicationContext)
            }
            wallpaper.isLiked = true
            return "Wallpaper saved"
        }
        return "Wallpaper save ERROR"
    }

    private fun deleteWallpaper(wallpaper: PickUpImage): String{
        if(wallpaper.url != null && wallpaper.description != null){
            val localWallpaper = LocalWallpaper(
                id = wallpaper.localID,
                description = wallpaper.description,
                image_url =  wallpaper.url,
            )
            deleteWallpaper(localWallpaper)
            viewModelScope.launch {
                BitmapSaveManager.deleteImageWallpaper(wallpaper, application.applicationContext)
            }
            wallpaper.isLiked = false
            return "Wallpaper deleted"
        }
        return "Wallpaper delete ERROR"
    }

    private fun saveWallpaper(wallpaper: LocalWallpaper) = viewModelScope.launch {
        imageRepository.insert(wallpaper)
    }

    fun getSavedWallpaper() = imageRepository.getSavedWallpaper()

    fun deleteWallpaper(wallpaper: LocalWallpaper) = viewModelScope.launch {
        if(wallpaper.id != null)
            imageRepository.delete(wallpaper)
        else imageRepository.deleteByUrl(wallpaper.image_url)
    }

//    fun alreadyHaveWallpaper(url: String) : LiveData<Boolean>{
//        val result = MutableLiveData<Boolean>()
//        viewModelScope.launch {
//            result.value = imageRepository.alreadyHave(url)
//        }
//        return result
//
//    }

    suspend fun alreadyHaveWallpaper(url: String) : MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        result.value = imageRepository.alreadyHave(url)
        return result
    }



}