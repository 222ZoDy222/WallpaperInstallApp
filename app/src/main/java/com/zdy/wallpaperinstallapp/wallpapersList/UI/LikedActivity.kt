package com.zdy.wallpaperinstallapp.wallpapersList.UI

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.zdy.wallpaperinstallapp.DB.WallpaperDatabase
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.databinding.ActivityLikedBinding
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import com.zdy.wallpaperinstallapp.pickUpWallpaper.UI.SelectWallpaperActivity
import com.zdy.wallpaperinstallapp.utils.Resource
import com.zdy.wallpaperinstallapp.wallpapersList.LikedList.ViewModel.WallpaperLikedListFactory
import com.zdy.wallpaperinstallapp.wallpapersList.LikedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.UI.ImagesAdapter
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.UI.ItemRecycle
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.ViewModel.RecycleViewModel
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.ViewModel.RecycleViewModelFactory
import com.zdy.wallpaperinstallapp.wallpapersList.WebList.ViewModel.WallpaperListFactory
import com.zdy.wallpaperinstallapp.wallpapersList.WebList.ViewModel.WallpaperListViewModel
import kotlinx.coroutines.launch

class LikedActivity : AppCompatActivity() {


    val imagesRepository: ImagesRepository by lazy {
        val repository = ImagesRepository(WallpaperDatabase(this))
        repository
    }

    val mViewModelLiked: WallpaperLikedListViewModel by lazy {
        ViewModelProvider(this,
            WallpaperLikedListFactory(application,imagesRepository)
        )[WallpaperLikedListViewModel::class.java]
    }

    val mViewModel: WallpaperListViewModel by lazy {
        ViewModelProvider(this, WallpaperListFactory(application,imagesRepository))[WallpaperListViewModel::class.java]
    }

    val recycleViewModel : RecycleViewModel by lazy {
        ViewModelProvider(this, RecycleViewModelFactory(this.application,mViewModelLiked))[RecycleViewModel::class.java]
    }

    val imagesAdapter: ImagesAdapter by lazy {
        ImagesAdapter(lifecycleScope)
    }

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

        val menuHost: MenuHost = this
        menuHost.invalidateMenu()
    }

    fun addListeners() {

        recycleViewModel.getItemsRecycle().observe(this){ list->
            imagesAdapter.differ.submitList(list)
        }

        recycleViewModel.setOnUpdateItem { item->
            imagesAdapter.updateImage(item as ItemRecycle.RecycleWallpaperItem)
        }

        mViewModel.getImageToPickUp().observe(this){image ->
            if(image != null){
                val bundle = Bundle()
                bundle.putParcelable(SelectWallpaperActivity.WALLPAPER_TAG, image)
                val intent = Intent(this, SelectWallpaperActivity::class.java)
                intent.putExtras(bundle)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mViewModel.PickUpImage(null)
                selectWallpaperLauncher?.launch(intent)
            }

        }

        addSelectWallpaperListener()

        mViewModelLiked.getSavedWallpaper().observe(this){wallpapers->
            recycleViewModel.setLocalList(wallpapers)
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
                        recycleViewModel.checkForUpdates(imageToUpdate)
                    }
                }

            }

        }
    }

    fun setupRecycleView() {

        val recycle = binding.rcViewAdapter
        recycle.let {

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
            mViewModel.PickUpImage(image)
        }


        imagesAdapter.setOnItemLikeClickListener {itemRecycle->
            recycleViewModel.onLikeImage(itemRecycle)
        }


    }


}