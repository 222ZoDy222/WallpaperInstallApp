package com.zdy.wallpaperinstallapp.WallpapersList.LikedList.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.WallpapersList.RecycleView.ViewModel.RecycleViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.RecycleView.ViewModel.RecycleViewModelFactory
import com.zdy.wallpaperinstallapp.WallpapersList.UI.FragmentList
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.Interfaces.IGetViewModelList
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.Interfaces.INavigate
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.databinding.FragmentListLikedBinding


class ListFragmentLiked : FragmentList() {



    lateinit var binding: FragmentListLikedBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListLikedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.invalidateMenu()


    }

    override fun addListeners(){
        super.addListeners()
        mViewModelLiked.getSavedWallpaper().observe(viewLifecycleOwner){wallpapers->
            recycleViewModel.setLocalList(wallpapers)
            setHaveWallpapers(wallpapers.isNotEmpty())
        }
    }

    private fun setHaveWallpapers(value: Boolean){
        binding.noLikedText.visibility = if(value) View.GONE else View.VISIBLE
    }


}