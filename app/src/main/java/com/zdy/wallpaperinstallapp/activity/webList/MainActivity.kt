package com.zdy.wallpaperinstallapp.activity.webList

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
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.zdy.wallpaperinstallapp.DB.WallpaperDatabase
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.databinding.ActivityMainBinding
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.SelectWallpaperActivity
import com.zdy.wallpaperinstallapp.utils.Resource
import com.zdy.wallpaperinstallapp.activity.likedList.ViewModel.WallpaperLikedListFactory
import com.zdy.wallpaperinstallapp.activity.likedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.UI.ImagesAdapter
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.UI.ItemRecycle
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.ViewModel.RecycleViewModel
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.ViewModel.RecycleViewModelFactory
import com.zdy.wallpaperinstallapp.activity.likedList.LikedActivity
import com.zdy.wallpaperinstallapp.activity.webList.ViewModel.WallpaperListFactory
import com.zdy.wallpaperinstallapp.activity.webList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.inheritance.WallpaperActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : WallpaperActivity() {


    private val mViewModel : WallpaperListViewModel by viewModels()

//    val mViewModel: WallpaperListViewModel by lazy {
//        ViewModelProvider(this, WallpaperListFactory(application,imagesRepository))[WallpaperListViewModel::class.java]
//    }

    val recycleViewModel : RecycleViewModel by lazy {
        ViewModelProvider(this, RecycleViewModelFactory(this.application, imagesRepository))[RecycleViewModel::class.java]
    }

    val imagesAdapter: ImagesAdapter by lazy {
        ImagesAdapter()
    }




    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ResourcesCompat.getFont(this,R.font.bicubik)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuHost : MenuHost = this

        menuHost.invalidateMenu()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.action_bar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId){
                    R.id.goLiked_button ->{
                        goLikedActivity()
                        true
                    }
                    else -> false
                }
            }

        }, this)
        supportActionBar?.show()

        setupRecycleView()
        addListeners()

    }

    override fun onSupportNavigateUp(): Boolean {
        //return navController.navigateUp() || super.onSupportNavigateUp()
        return true
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
                mViewModel.PickUpImage(null, applicationContext)
                selectWallpaperLauncher?.launch(intent)
            }

        }

        addSelectWallpaperListener()

        mViewModel.getImageRequest().observe(this){imageRequest ->

            binding.loadbar.visibility = View.GONE

        }


        mViewModel.getErrorMessage().observe(this){error->
            showErrorMessage(error)
        }

        binding.reloadButton.setOnClickListener {
            mViewModel.getRandomImages(applicationContext)
        }


        mViewModel.getImageRequest().observe(this){response->
            when(response){
                is Resource.Success ->{
                    response.data?.let {
                        recycleViewModel.setWebList(it)
                    }
                    Loading(false)
                }
                is Resource.Error ->{
                    Loading(false)
                    // TODO: Show Error message
                }
                is Resource.Loading ->{
                    Loading(true)
                }
            }

        }

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
            mViewModel.PickUpImage(image,applicationContext)
        }


        imagesAdapter.setOnItemLikeClickListener {itemRecycle->
            recycleViewModel.onLikeImage(itemRecycle)
        }

        imagesAdapter.setOnRefreshClickListener {
            mViewModel.getRandomImages(applicationContext)
        }
    }

    private fun showErrorMessage(type: WallpaperListViewModel.ErrorType?){

        binding.reloadContainer.visibility = View.VISIBLE
        when(type){
            WallpaperListViewModel.ErrorType.noInternetConnection -> binding.errorTextView.text =
                getString(
                    R.string.noInternetConnection
                )
            WallpaperListViewModel.ErrorType.requestError -> binding.errorTextView.text =
                getString(
                    R.string.requestError
                )
            WallpaperListViewModel.ErrorType.convertionError -> binding.errorTextView.text =
                getString(
                    R.string.convertionError
                )

            else -> {
                binding.reloadContainer.visibility = View.GONE
            }
        }
    }


    private fun Loading(value: Boolean){
        if(value)
            binding.loadbar.visibility = View.VISIBLE
        else
            binding.loadbar.visibility = View.GONE
    }

    private fun goLikedActivity(){
        val intent = Intent(this, LikedActivity::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent)
    }

}