package com.zdy.wallpaperinstallapp.PickUpWallpaper.UI

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.Logger.AppLogger
import com.zdy.wallpaperinstallapp.PickUpWallpaper.Interfaces.IGetViewModelPickUp
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.PickUpWallpaperViewModel
import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.SetWallpaperViewModel
import com.zdy.wallpaperinstallapp.R

class SelectWallpaperActivity : AppCompatActivity(), IGetViewModelPickUp {



    val mViewModel : PickUpWallpaperViewModel by lazy{
        ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(application)
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

        val image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
             intent.extras?.getParcelable(IMAGE_TAG, PickUpImage::class.java)
        } else{
            @Suppress("DEPRECATION")
            intent.extras?.getParcelable<PickUpImage>(IMAGE_TAG)
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



    }





    companion object{
        const val IMAGE_TAG = "EXPORTED_IMAGE"
    }

    override fun getViewModelPickUp(): PickUpWallpaperViewModel = mViewModel
    override fun getViewModelSet(): SetWallpaperViewModel = mViewModelSet
}