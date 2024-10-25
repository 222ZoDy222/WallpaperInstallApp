package com.zdy.wallpaperinstallapp.UI

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.DB.WallpaperDatabase
import com.zdy.wallpaperinstallapp.WallpapersList.LikedList.Interfaces.IGetLikedViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.LikedList.ViewModel.WallpaperLikedListFactory
import com.zdy.wallpaperinstallapp.WallpapersList.LikedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository

open class WallpaperActivity : AppCompatActivity(), IGetLikedViewModel {

    val imagesRepository: ImagesRepository by lazy {
        val repository = ImagesRepository(WallpaperDatabase(this))
        repository
    }

    val mViewModelLiked: WallpaperLikedListViewModel by lazy {
        ViewModelProvider(this, WallpaperLikedListFactory(application,imagesRepository))[WallpaperLikedListViewModel::class.java]
    }

    override fun getLikedViewModel(): WallpaperLikedListViewModel = mViewModelLiked

}