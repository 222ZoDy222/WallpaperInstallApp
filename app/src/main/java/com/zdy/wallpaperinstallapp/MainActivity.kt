package com.zdy.wallpaperinstallapp

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.zdy.wallpaperinstallapp.ActivityExtensions.ActivityFragment
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.INavigate
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListFactory
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListViewModel

class MainActivity : ActivityFragment(), INavigate {




    private lateinit var mViewModel: WallpaperListViewModel

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this,R.id.nav_host)
        mViewModel = ViewModelProvider(this, WallpaperListFactory(application))[WallpaperListViewModel::class.java]



    }

    override fun NavigateToLikedList() {
        navController.navigate(R.id.action_listFragment_to_listFragmentLiked)
    }
}