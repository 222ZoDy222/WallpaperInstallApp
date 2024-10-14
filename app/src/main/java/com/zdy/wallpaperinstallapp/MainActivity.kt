package com.zdy.wallpaperinstallapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.IGetViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.INavigate
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListFactory
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.Web.Requests.ImageRepository

class MainActivity : AppCompatActivity(), INavigate, IGetViewModel {




    val mViewModel: WallpaperListViewModel by lazy {
        val repository = ImageRepository()
        ViewModelProvider(this, WallpaperListFactory(application,repository))[WallpaperListViewModel::class.java]
    }

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val repository = ImageRepository()
//        mViewModel = ViewModelProvider(this, WallpaperListFactory(application,repository))[WallpaperListViewModel::class.java]


        navController = Navigation.findNavController(this,R.id.nav_host)



    }

    override fun NavigateToLikedList() {
        navController.navigate(R.id.action_listFragment_to_listFragmentLiked)
    }

    override fun getViewModel(): WallpaperListViewModel {
        return mViewModel
    }
}