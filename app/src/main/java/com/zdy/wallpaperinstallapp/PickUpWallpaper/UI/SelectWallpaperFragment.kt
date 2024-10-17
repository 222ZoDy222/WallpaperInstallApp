package com.zdy.wallpaperinstallapp.PickUpWallpaper.UI

import android.R
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zdy.wallpaperinstallapp.PickUpWallpaper.Interfaces.IGetViewModelPickUp
import com.zdy.wallpaperinstallapp.PickUpWallpaper.Objects.PickUpImage
import com.zdy.wallpaperinstallapp.PickUpWallpaper.ViewModel.PickUpWallpaperViewModel
import com.zdy.wallpaperinstallapp.Web.Repository.ImagesRepository

import com.zdy.wallpaperinstallapp.databinding.FragmentSelectWallpaperBinding


class SelectWallpaperFragment : Fragment() {


    lateinit var binding : FragmentSelectWallpaperBinding

    lateinit var mViewModel : PickUpWallpaperViewModel

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
                context?.let {context ->
                    binding.backgroundImage.setImageDrawable(drawable)
                    val imageWidth = drawable.intrinsicWidth
                    val imageHeight = drawable.intrinsicHeight
                    val viewWidth_test = binding.imageContainer.width
                    val viewHeight_test = binding.imageContainer.height
                    if(viewWidth_test == 0 || viewHeight_test == 0){
                        var temp = 0
                    }
                    val viewWidth = context.resources.displayMetrics.widthPixels
                    val viewHeight = context.resources.displayMetrics.heightPixels
                    // Scaling image
                    mViewModel.setImageToFullScreen(imageWidth, imageHeight, viewWidth, viewHeight)
                    binding.backgroundImage.imageMatrix = mViewModel.getMatrix()
                }

            } else{
                binding.loadbar.visibility = View.VISIBLE
                val url = mViewModel.getUrl()

                // TODO: Progress bar
                url?.let{
                    context?.let {context ->
                        ImagesRepository.LoadBitmapByURL(
                            context,
                            url,
                            object : CustomTarget<Bitmap>(){
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    mViewModel.SetBackgroundImage(resource)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    // TODO: Error message
                                }

                            })
                    }
                }

            }


        }


    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = (requireActivity() as IGetViewModelPickUp).getViewModel()

        // If user clicked so fast
        binding.backgroundImage.viewTreeObserver.addOnGlobalLayoutListener { mViewModel.updateDrawableImage() }


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





    companion object {

        @JvmStatic
        fun newInstance() =
            SelectWallpaperFragment()
    }
}