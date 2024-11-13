package com.zdy.wallpaperinstallapp.activity.likedList

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.zdy.wallpaperinstallapp.databinding.ActivityLikedBinding
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.SelectWallpaperActivity
import com.zdy.wallpaperinstallapp.activity.likedList.viewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.activity.recycleView.ui.ImagesAdapter
import com.zdy.wallpaperinstallapp.activity.recycleView.ui.ItemRecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LikedActivity : AppCompatActivity() {


    private val viewModel: WallpaperLikedListViewModel by viewModels()

    val imagesAdapter: ImagesAdapter = ImagesAdapter()

    lateinit var binding : ActivityLikedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecycleView()
        addListeners()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        // TODO: Get back from this activity using Actionbar

        val menuHost: MenuHost = this
        menuHost.invalidateMenu()
    }

    fun addListeners() {

        viewModel.getItemsRecycle().observe(this){ list->
            imagesAdapter.differ.submitList(list)
        }

        viewModel.setOnUpdateItem { item->
            imagesAdapter.updateImage(item as ItemRecycle.RecycleWallpaperItem)
        }

        viewModel.getImageToPickUp().observe(this){ image ->
            if(image != null){
                val bundle = Bundle()
                bundle.putParcelable(SelectWallpaperActivity.WALLPAPER_TAG, image)
                val intent = Intent(this, SelectWallpaperActivity::class.java)
                intent.putExtras(bundle)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                viewModel.pickUpImage(null, applicationContext)
                selectWallpaperLauncher?.launch(intent)
            }
        }

        addSelectWallpaperListener()

        viewModel.getSavedWallpaper().observe(this){ wallpapers->
            viewModel.setLocalList(wallpapers, applicationContext)
            setHaveWallpapers(wallpapers.isNotEmpty())
        }

    }

    private fun setHaveWallpapers(value: Boolean){
        binding.noLikedText.visibility = if(value) View.GONE else View.VISIBLE
    }

    private var selectWallpaperLauncher : ActivityResultLauncher<Intent>? = null


    fun addSelectWallpaperListener(){
        selectWallpaperLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){  result->
            if(result.resultCode == RESULT_OK){
                val imageToUpdate : PickUpImage? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(SelectWallpaperActivity.WALLPAPER_TAG, PickUpImage::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    result.data?.getParcelableExtra(SelectWallpaperActivity.WALLPAPER_TAG)
                }
                imageToUpdate?.let {
                    lifecycleScope.launch {
                        viewModel.checkForUpdates(imageToUpdate)
                    }
                }
            }
        }
    }

    fun setupRecycleView() {

        binding.rcViewAdapter.let {

            val gridManager = GridLayoutManager(this,2)
            gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    return if(imagesAdapter.isButtonType(position)) 1 else 1
                }

            }
            it.layoutManager = gridManager
            it.adapter = imagesAdapter

        }

        imagesAdapter.setOnItemClickListener { image->
            viewModel.pickUpImage(image,applicationContext)
        }

        imagesAdapter.setOnItemLikeClickListener {itemRecycle->
            viewModel.onLikeImage(itemRecycle, applicationContext)
        }
    }


}