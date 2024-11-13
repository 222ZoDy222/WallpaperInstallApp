package com.zdy.wallpaperinstallapp.activity.likedList.ViewModel

import com.zdy.wallpaperinstallapp.inheritance.ListViewModel
import com.zdy.wallpaperinstallapp.models.LocalSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WallpaperLikedListViewModel @Inject constructor(
    val imageRepository: ImagesRepository
) : ListViewModel() {


    val saveModel: LocalSaveModel = LocalSaveModel(imageRepository)


    fun getSavedWallpaper() = imageRepository.getSavedWallpaper()


}