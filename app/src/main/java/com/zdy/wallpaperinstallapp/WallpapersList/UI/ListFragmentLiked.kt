package com.zdy.wallpaperinstallapp.WallpapersList.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.MainActivity
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListFactory
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.databinding.FragmentListLikedBinding


class ListFragmentLiked : Fragment() {


    private lateinit var mViewModel: WallpaperListViewModel
    lateinit var binding: FragmentListLikedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: This is DRY bad practice (should create inheritor mb)
        mViewModel = ViewModelProvider(requireActivity(),
            WallpaperListFactory(requireActivity().application)
        )[WallpaperListViewModel::class.java]


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListLikedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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