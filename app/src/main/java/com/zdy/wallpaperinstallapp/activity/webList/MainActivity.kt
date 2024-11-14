package com.zdy.wallpaperinstallapp.activity.webList

import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.databinding.ActivityMainBinding
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.SelectWallpaperActivity
import com.zdy.wallpaperinstallapp.utils.Resource
import com.zdy.wallpaperinstallapp.inheritance.recycleView.ui.ImagesAdapter
import com.zdy.wallpaperinstallapp.inheritance.recycleView.ui.ItemRecycle
import com.zdy.wallpaperinstallapp.activity.likedList.LikedActivity
import com.zdy.wallpaperinstallapp.activity.webList.viewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.utils.extensions.getPickUpImage

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val mViewModel : WallpaperListViewModel by viewModels()


    private val imagesAdapter: ImagesAdapter = ImagesAdapter()




    private lateinit var binding : ActivityMainBinding

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

        mViewModel.getRandomImages(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        //return navController.navigateUp() || super.onSupportNavigateUp()
        return true
    }


    private fun addListeners() {

        mViewModel.getItemsRecycle().observe(this){ list->
            imagesAdapter.differ.submitList(list)
        }

        mViewModel.setOnUpdateItem { item->
            imagesAdapter.updateImage(item as ItemRecycle.RecycleWallpaperItem)
        }

        mViewModel.getImageToPickUp().observe(this){image ->
            if(image != null){
                val bundle = Bundle()
                bundle.putParcelable(SelectWallpaperActivity.WALLPAPER_TAG, image)
                val intent = Intent(this, SelectWallpaperActivity::class.java)
                intent.putExtras(bundle)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mViewModel.pickUpImage(null, applicationContext)
                selectWallpaperLauncher?.launch(intent)
            }

        }

        addSelectWallpaperListener()

        mViewModel.getImageRequest().observe(this){_ ->
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
                        mViewModel.setWebList(it, applicationContext)
                    }
                    loading(false)
                }
                is Resource.Error ->{
                    loading(false)
                }
                is Resource.Loading ->{
                    loading(true)
                }
            }

        }

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
                        mViewModel.checkForUpdates(imageToUpdate)
                    }
                }

            }

        }
    }

    private fun setupRecycleView() {

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
            mViewModel.pickUpImage(image, applicationContext)
        }


        imagesAdapter.setOnItemLikeClickListener {itemRecycle->
            mViewModel.onLikeImage(itemRecycle, applicationContext)
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


    private fun loading(value: Boolean){
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