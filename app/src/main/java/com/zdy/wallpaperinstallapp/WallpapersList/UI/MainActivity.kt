package com.zdy.wallpaperinstallapp.WallpapersList.UI

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.zdy.wallpaperinstallapp.PickUpWallpaper.UI.SelectWallpaperActivity
import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.PickUpWallpaperViewModel
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.IGetViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.INavigate
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.IPickUpImage
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListFactory
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.Web.Objects.NekoImage
import com.zdy.wallpaperinstallapp.Web.Requests.ImageRepository

class MainActivity : AppCompatActivity(), INavigate, IGetViewModel {




    val mViewModel: WallpaperListViewModel by lazy {
        val repository = ImageRepository()
        ViewModelProvider(this, WallpaperListFactory(application,repository))[WallpaperListViewModel::class.java]
    }

    lateinit var pickUpViewModel : PickUpWallpaperViewModel


    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        pickUpViewModel = ViewModelProvider(
            this as ViewModelStoreOwner,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[PickUpWallpaperViewModel::class.java]


        pickUpViewModel.imageToPickUp.observe(this){image ->
            if(image != null){
                val temp = pickUpViewModel.imageToPickUp.value
                val intent = Intent(this,SelectWallpaperActivity::class.java)
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