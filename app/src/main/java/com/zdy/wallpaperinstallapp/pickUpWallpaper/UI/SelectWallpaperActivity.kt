package com.zdy.wallpaperinstallapp.pickUpWallpaper.UI

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.logger.AppLogger
import com.zdy.wallpaperinstallapp.pickUpWallpaper.Interfaces.IGetViewModelPickUp
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel.PickUpWallpaperViewModel
import com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel.PickUpWallpaperViewModelFactory
import com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel.SetWallpaperViewModel
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.ui.WallpaperActivity
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
//        val image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//             intent.extras?.getParcelable(WALLPAPER_TAG, PickUpImage::class.java)
//        } else{
//            @Suppress("DEPRECATION")
//            intent.extras?.getParcelable<PickUpImage>(WALLPAPER_TAG)
//        }

        val image : PickUpImage? = intent.extras?.getParcelableImage()

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

private fun Bundle?.getParcelableImage(): PickUpImage? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return this?.getParcelable(SelectWallpaperActivity.WALLPAPER_TAG, PickUpImage::class.java)
    } else{
        @Suppress("DEPRECATION")
        return this?.getParcelable<PickUpImage>(SelectWallpaperActivity.WALLPAPER_TAG)
    }
}


