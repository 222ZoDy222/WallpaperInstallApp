package com.zdy.wallpaperinstallapp.PickUpWallpaper.UI

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.core.os.BuildCompat
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.Logger.AppLogger
import com.zdy.wallpaperinstallapp.PickUpWallpaper.Interfaces.IGetViewModelPickUp
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.PickUpWallpaperViewModel
import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.PickUpWallpaperViewModelFactory
import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.SetWallpaperViewModel
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.UI.WallpaperActivity
import androidx.activity.addCallback

class SelectWallpaperActivity : WallpaperActivity(), IGetViewModelPickUp {

    val mViewModel : PickUpWallpaperViewModel by lazy{
        ViewModelProvider(this,
            PickUpWallpaperViewModelFactory(application,mViewModelLiked)
        )[PickUpWallpaperViewModel::class.java]
    }

    val mViewModelSet : SetWallpaperViewModel by lazy{
        ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[SetWallpaperViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_wallpaper)
        supportActionBar?.hide()
        val image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
             intent.extras?.getParcelable(WALLPAPER_TAG, PickUpImage::class.java)
        } else{
            @Suppress("DEPRECATION")
            intent.extras?.getParcelable<PickUpImage>(WALLPAPER_TAG)
        }

        if(savedInstanceState == null){
            if(image != null){
                mViewModel.SelectImage(image)
            } else {
                AppLogger.Log("Select image is null")
                finish()
            }
        } else{
            finish()
        }

        onBackPressedDispatcher.addCallback(this) {
            completeActivity()
            finish()
        }

    }


    private fun completeActivity(){
        intent.putExtra(WALLPAPER_TAG,mViewModel.selectedImage.value)
        setResult(RESULT_OK,intent)
    }





    companion object{
        const val WALLPAPER_TAG = "EXPORTED_WALLPAPER_TAG"
    }

    override fun getViewModelPickUp(): PickUpWallpaperViewModel = mViewModel
    override fun getViewModelSet(): SetWallpaperViewModel = mViewModelSet

}