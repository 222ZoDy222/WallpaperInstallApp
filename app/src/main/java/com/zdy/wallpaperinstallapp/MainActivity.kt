package com.zdy.wallpaperinstallapp

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.ActivityExtensions.ActivityFragment
import com.zdy.wallpaperinstallapp.WallpapersList.UI.ListFragment
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListFactory
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListViewModel

class MainActivity : ActivityFragment() {


    // OnCleared() didn't work
    // private val vm: MainViewModel by viewModels()

    private lateinit var mViewModel: WallpaperListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProvider(this, WallpaperListFactory(application))[WallpaperListViewModel::class.java]
        mViewModel.menuLiveData.observe(this) {
            val placeHolder = R.id.place_holder
            val fragment = ListFragment.newInstance(it == WallpaperListViewModel.menu.wallpaperInternet)
            when(it){
                WallpaperListViewModel.menu.wallpaperInternet ->
                    SwitchFragment(placeHolder,fragment)
                WallpaperListViewModel.menu.wallpaperLiked ->
                    AddFragment(placeHolder,fragment)
            }
        }


    }
}