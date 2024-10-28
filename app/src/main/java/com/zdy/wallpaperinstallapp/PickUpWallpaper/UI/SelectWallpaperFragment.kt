package com.zdy.wallpaperinstallapp.PickUpWallpaper.UI

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.zdy.wallpaperinstallapp.PickUpWallpaper.Interfaces.IGetViewModelPickUp
import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.PickUpWallpaperViewModel
import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.SetWallpaperViewModel
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.databinding.FragmentSelectWallpaperBinding


class SelectWallpaperFragment : Fragment() {


    lateinit var binding : FragmentSelectWallpaperBinding

    lateinit var mViewModel : PickUpWallpaperViewModel
    lateinit var mViewModelSetWallpaper : SetWallpaperViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectWallpaperBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        mViewModel.getBackgroundDrawable().observe(viewLifecycleOwner){ drawable->
            if(drawable != null){
                binding.loadbar.visibility = View.GONE
                showButtons(true)
                binding.backgroundImage.setImageDrawable(drawable)
                mViewModel.setImageToFullScreen(drawable)
                binding.backgroundImage.imageMatrix = mViewModel.getMatrix()

            } else{
                binding.loadbar.visibility = View.VISIBLE
                showButtons(false)
            }


        }


    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = (requireActivity() as IGetViewModelPickUp).getViewModelPickUp()
        mViewModelSetWallpaper = (requireActivity() as IGetViewModelPickUp).getViewModelSet()
        // If user clicked so fast
        binding.backgroundImage.viewTreeObserver.addOnGlobalLayoutListener { mViewModel.updateDrawableImage() }

        binding.settingsWallpaperButton.setOnClickListener {
            mViewModel.selectedImage.value?.let { image ->
                context?.let {context ->
                    mViewModelSetWallpaper.setWallpaper(
                        image,
                        context
                    )
                }
            }
        }

        binding.likeButtonInclude.likeButton.setOnClickListener {
            mViewModel.onLikeImage()
        }

        binding.shareWallpaperButton.setOnClickListener {

            mViewModel.selectedImage.value?.let {image->
                context?.let {context->
                    mViewModelSetWallpaper.ShareWallpaper(image,context)
                }
            }
        }

        mViewModel.selectedImage.observe(viewLifecycleOwner) { image ->
            image?.let {
                val imageID = if(image.isLiked) R.drawable.liked_icon else R.drawable.like_icon
                val iconLike: Drawable =
                    resources.getDrawable(imageID, context?.theme);
                binding.likeButtonInclude.likeButton.setImageDrawable(iconLike)
                binding.bottomSheetInclude.descriptionText.text = image.description
            }
        }


        // Listener for moving wallpaper image
        binding.backgroundImage.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Set touch coordinates
                    mViewModel.setLastTouch(event.x, event.y)
                }
                MotionEvent.ACTION_MOVE -> {
                    // Update matrix
                    binding.backgroundImage.imageMatrix = mViewModel.handleMove(event.x, event.y)
                }
            }
            true
        }

    }


    private fun showButtons(value: Boolean){
        val visibleValue = if(value) View.VISIBLE else View.INVISIBLE
        binding.settingsWallpaperButton.visibility = visibleValue
        binding.shareWallpaperButton.visibility = visibleValue
        binding.likeButtonInclude.likeButton.visibility = visibleValue
    }


}