package com.zdy.wallpaperinstallapp.WallpapersList.LikedList.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.Interfaces.IGetViewModelList
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.databinding.FragmentListLikedBinding


class ListFragmentLiked : Fragment() {


    private lateinit var mViewModel: WallpaperListViewModel
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
        // TODO: This is DRY bad practice (should create inheritor mb)
        mViewModel = (activity as IGetViewModelList).getViewModel()
        addListeners()
    }

    private fun addListeners(){

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ListFragmentLiked()
    }
}