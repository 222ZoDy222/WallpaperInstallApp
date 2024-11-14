package com.zdy.wallpaperinstallapp.activity.wallpaperDetails

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.databinding.ActivitySelectWallpaperBinding
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel.PickUpWallpaperViewModel
import com.zdy.wallpaperinstallapp.utils.extensions.getParcelableImage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectWallpaperActivity : AppCompatActivity() {

    val mViewModel : PickUpWallpaperViewModel by viewModels()


    lateinit var binding : ActivitySelectWallpaperBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectWallpaperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val image : PickUpImage? = intent.extras?.getParcelableImage(WALLPAPER_TAG)

        if(savedInstanceState == null){
            if(image != null){
                mViewModel.selectImage(image, applicationContext)
            } else {
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            completeActivity()
            finish()
        }


        mViewModel.getBackgroundDrawable().observe(this){ drawable->
            if(drawable != null){
                binding.loadbar.visibility = View.GONE
                showButtons(true)
                binding.backgroundImage.setImageDrawable(drawable)

            } else{
                binding.loadbar.visibility = View.VISIBLE
                showButtons(false)
            }


        }


        binding.settingsWallpaperButton.setOnClickListener {
            mViewModel.selectedImage.value?.let { image ->
                this.let {context ->
                    mViewModel.setWallpaper(
                        image,
                        context
                    )
                }
            }
        }

        // Show/Hide BottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetInclude.bottomSheet)

        binding.bottomSheetInclude.bottomSheetHeader.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        binding.likeButtonInclude.likeButton.setOnClickListener {
            mViewModel.onLikeImage(applicationContext)
        }

        binding.shareWallpaperButton.setOnClickListener {

            mViewModel.selectedImage.value?.let {image->
                this.let {context->
                    mViewModel.shareWallpaper(image,context)
                }
            }
        }

        mViewModel.selectedImage.observe(this) { image ->
            image?.let {
                val imageID = if(image.isLiked) R.drawable.liked_icon else R.drawable.like_icon
                val iconLike: Drawable? = ResourcesCompat.getDrawable(resources, imageID, applicationContext.theme)
                binding.likeButtonInclude.likeButton.setImageDrawable(iconLike)
                binding.bottomSheetInclude.descriptionText.text = image.description
            }
        }

    }


    private fun completeActivity(){
        intent.putExtra(WALLPAPER_TAG,mViewModel.selectedImage.value)
        setResult(RESULT_OK,intent)
    }

    private fun showButtons(value: Boolean){
        val visibleValue = if(value) View.VISIBLE else View.INVISIBLE
        binding.settingsWallpaperButton.visibility = visibleValue
        binding.shareWallpaperButton.visibility = visibleValue
        binding.likeButtonInclude.likeButton.visibility = visibleValue
    }




    companion object{
        const val WALLPAPER_TAG = "EXPORTED_WALLPAPER_TAG"
    }



}

