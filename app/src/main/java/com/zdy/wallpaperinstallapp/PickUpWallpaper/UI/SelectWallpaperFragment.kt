package com.zdy.wallpaperinstallapp.PickUpWallpaper.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zdy.wallpaperinstallapp.Web.Objects.NekoImage
import com.zdy.wallpaperinstallapp.databinding.FragmentSelectWallpaperBinding

class SelectWallpaperFragment : Fragment() {


    lateinit var binding : FragmentSelectWallpaperBinding

    private lateinit var nekoImage : NekoImage

    init {


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectWallpaperBinding.inflate(inflater)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SelectWallpaperFragment()
    }
}