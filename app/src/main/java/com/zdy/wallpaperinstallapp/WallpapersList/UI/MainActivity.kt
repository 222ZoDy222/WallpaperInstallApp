package com.zdy.wallpaperinstallapp.WallpapersList.UI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zdy.wallpaperinstallapp.PickUpWallpaper.Objects.PickUpImage
import com.zdy.wallpaperinstallapp.PickUpWallpaper.UI.SelectWallpaperActivity
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.IGetViewModelList
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.INavigate
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListFactory
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.Web.Requests.ImageRepository

class MainActivity : AppCompatActivity(), INavigate, IGetViewModelList {




    val mViewModel: WallpaperListViewModel by lazy {
        val repository = ImageRepository()
        ViewModelProvider(this, WallpaperListFactory(application,repository))[WallpaperListViewModel::class.java]
    }

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController


        mViewModel.getImageToPickUp().observe(this){image ->
            if(image != null){
                val pickUpImage = PickUpImage(null, image.url, image.description)
                val bundle = Bundle()
                bundle.putParcelable(SelectWallpaperActivity.IMAGE_TAG, pickUpImage)
                val intent = Intent(this,SelectWallpaperActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }

        }

    }

    override fun NavigateToLikedList() {
        navController.navigate(R.id.action_listFragment_to_listFragmentLiked)
    }

    override fun getViewModel(): WallpaperListViewModel {
        return mViewModel
    }




}