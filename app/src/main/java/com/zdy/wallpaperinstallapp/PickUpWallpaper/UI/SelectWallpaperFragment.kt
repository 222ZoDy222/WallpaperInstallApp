package com.zdy.wallpaperinstallapp.PickUpWallpaper.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.PickUpWallpaperViewModel
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.Web.Objects.NekoImage
import com.zdy.wallpaperinstallapp.databinding.FragmentSelectWallpaperBinding

class SelectWallpaperFragment : Fragment() {


    lateinit var binding : FragmentSelectWallpaperBinding

    private lateinit var nekoImage : NekoImage

    lateinit var pickUpViewModel : PickUpWallpaperViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectWallpaperBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickUpViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[PickUpWallpaperViewModel::class.java]

        var temp = pickUpViewModel.imageToPickUp.value

        pickUpViewModel.imageToPickUp.observe(viewLifecycleOwner){
            UpdateUI(it)
        }

    }

    private fun UpdateUI(image: NekoImage){
        binding.apply {
            Glide.with(requireContext()).load(image.image_url).into(backgroundImage)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SelectWallpaperFragment()
    }
}