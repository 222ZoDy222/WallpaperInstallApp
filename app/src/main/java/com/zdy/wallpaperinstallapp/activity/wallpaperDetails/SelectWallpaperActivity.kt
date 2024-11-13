package com.zdy.wallpaperinstallapp.activity.wallpaperDetails

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.databinding.ActivitySelectWallpaperBinding
import com.zdy.wallpaperinstallapp.logger.AppLogger
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.pickUpWallpaper.ViewModel.PickUpWallpaperViewModel
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


        val image : PickUpImage? = intent.extras?.getParcelableImage()

        if(savedInstanceState == null){
            if(image != null){
                mViewModel.selectImage(image, applicationContext)
            } else {
                AppLogger.Log("Select image is null")
                finish()
            }
        } else{
            finish()
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
                mViewModel.setImageToFullScreen(drawable, applicationContext)
                binding.backgroundImage.imageMatrix = mViewModel.getMatrix()

            } else{
                binding.loadbar.visibility = View.VISIBLE
                showButtons(false)
            }


        }



        // If user clicked so fast
        binding.backgroundImage.viewTreeObserver.addOnGlobalLayoutListener { mViewModel.updateDrawableImage() }

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
        binding.bottomSheetInclude.bottomSheet.findViewById<ImageView>(R.id.bottom_sheet_header).setOnClickListener {
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
                val iconLike: Drawable =
                    resources.getDrawable(imageID, this.theme);
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

private fun Bundle?.getParcelableImage(): PickUpImage? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return this?.getParcelable(SelectWallpaperActivity.WALLPAPER_TAG, PickUpImage::class.java)
    } else{
        @Suppress("DEPRECATION")
        return this?.getParcelable<PickUpImage>(SelectWallpaperActivity.WALLPAPER_TAG)
    }
}