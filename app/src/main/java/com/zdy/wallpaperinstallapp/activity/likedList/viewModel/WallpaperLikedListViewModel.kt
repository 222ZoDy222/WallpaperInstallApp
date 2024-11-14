package com.zdy.wallpaperinstallapp.activity.likedList.viewModel

import com.zdy.wallpaperinstallapp.inheritance.ListViewModel
import com.zdy.wallpaperinstallapp.models.localSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.repository.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WallpaperLikedListViewModel @Inject constructor(
    val imageRepository: ImagesRepository,
    localSaveModel: LocalSaveModel
) : ListViewModel(localSaveModel) {

    fun getSavedWallpaper() = imageRepository.getSavedWallpaper()


}