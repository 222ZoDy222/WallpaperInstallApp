package com.zdy.wallpaperinstallapp.wallpapersList.UI

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.ui.WallpaperActivity
import com.zdy.wallpaperinstallapp.wallpapersList.WebList.Interfaces.IGetViewModelList
import com.zdy.wallpaperinstallapp.wallpapersList.WebList.Interfaces.INavigate
import com.zdy.wallpaperinstallapp.wallpapersList.WebList.ViewModel.WallpaperListFactory
import com.zdy.wallpaperinstallapp.wallpapersList.WebList.ViewModel.WallpaperListViewModel

class MainActivity : WallpaperActivity(), INavigate, IGetViewModelList {




    val mViewModel: WallpaperListViewModel by lazy {
        ViewModelProvider(this, WallpaperListFactory(application,imagesRepository))[WallpaperListViewModel::class.java]
    }

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ResourcesCompat.getFont(this,R.font.bicubik)
        setContentView(R.layout.activity_main)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)



    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun NavigateToLikedList() {
        try{
            navController.navigate(R.id.action_listFragment_to_listFragmentLiked)
        }catch (ex:Exception){
            // User fast clicked many times on button to navigate
        }

    }

    override fun NavigateBack() {
        navController.navigateUp()
    }

    override fun getViewModel(): WallpaperListViewModel = mViewModel


}