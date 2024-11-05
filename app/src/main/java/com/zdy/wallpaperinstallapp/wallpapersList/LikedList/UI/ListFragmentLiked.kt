package com.zdy.wallpaperinstallapp.wallpapersList.LikedList.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import com.zdy.wallpaperinstallapp.wallpapersList.UI.FragmentList
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