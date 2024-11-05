package com.zdy.wallpaperinstallapp.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.DB.WallpaperDatabase
import com.zdy.wallpaperinstallapp.wallpapersList.LikedList.Interfaces.IGetLikedViewModel
import com.zdy.wallpaperinstallapp.wallpapersList.LikedList.ViewModel.WallpaperLikedListFactory
import com.zdy.wallpaperinstallapp.wallpapersList.LikedList.ViewModel.WallpaperLikedListViewModel
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