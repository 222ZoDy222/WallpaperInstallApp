package com.zdy.wallpaperinstallapp.activity.likedList

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.databinding.ActivityLikedBinding
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.SelectWallpaperActivity
import com.zdy.wallpaperinstallapp.activity.likedList.viewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.inheritance.recycleView.ui.ImagesAdapter
import com.zdy.wallpaperinstallapp.inheritance.recycleView.ui.ItemRecycle
import com.zdy.wallpaperinstallapp.utils.extensions.getPickUpImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LikedActivity : AppCompatActivity() {


    private val viewModel: WallpaperLikedListViewModel by viewModels()

    private val imagesAdapter: ImagesAdapter = ImagesAdapter()

    private lateinit var binding : ActivityLikedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecycleView()
        addListeners()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val menuHost: MenuHost = this
        menuHost.invalidateMenu()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId){
                    android.R.id.home ->{
                        finish()
                        true
                    }
                    else -> false
                }
            }

        }, this)
    }


    private fun addListeners() {

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


    private fun addSelectWallpaperListener(){
        selectWallpaperLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){  result->
            if(result.resultCode == RESULT_OK){
                val imageToUpdate : PickUpImage? = result.data.getPickUpImage(SelectWallpaperActivity.WALLPAPER_TAG)
                imageToUpdate?.let {
                    lifecycleScope.launch {
                        viewModel.checkForUpdates(imageToUpdate)
                    }
                }
            }
        }
    }

    private fun setupRecycleView() {

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