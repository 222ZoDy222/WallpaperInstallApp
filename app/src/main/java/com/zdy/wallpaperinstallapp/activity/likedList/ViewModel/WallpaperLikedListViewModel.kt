package com.zdy.wallpaperinstallapp.activity.likedList.ViewModel

import android.app.Application
import android.content.Context
import android.widget.ListView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.inheritance.ListViewModel
import com.zdy.wallpaperinstallapp.models.LocalSave.BitmapSaveManager
import com.zdy.wallpaperinstallapp.models.LocalSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.models.ObjectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import kotlinx.coroutines.launch

class WallpaperLikedListViewModel(
    val imageRepository: ImagesRepository
) : ListViewModel() {


    lateinit var saveModel: LocalSaveModel


    init {
        saveModel = LocalSaveModel(imageRepository)
    }








    fun getSavedWallpaper() = imageRepository.getSavedWallpaper()




}