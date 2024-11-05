package com.zdy.wallpaperinstallapp.wallpapersList.UI

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zdy.wallpaperinstallapp.pickUpWallpaper.UI.SelectWallpaperActivity
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.wallpapersList.LikedList.Interfaces.IGetLikedViewModel
import com.zdy.wallpaperinstallapp.wallpapersList.LikedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.wallpapersList.WebList.Interfaces.IGetViewModelList
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.UI.ImagesAdapter
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.UI.ItemRecycle
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.ViewModel.RecycleViewModel
import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.ViewModel.RecycleViewModelFactory
import com.zdy.wallpaperinstallapp.wallpapersList.WebList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import kotlinx.coroutines.launch

open class FragmentList : Fragment() {

    protected lateinit var mViewModel: WallpaperListViewModel
    protected  lateinit var mViewModelLiked: WallpaperLikedListViewModel

    val recycleViewModel : RecycleViewModel by lazy {
        ViewModelProvider(this, RecycleViewModelFactory(requireActivity().application,mViewModelLiked))[RecycleViewModel::class.java]
    }

    val imagesAdapter: ImagesAdapter by lazy {
        ImagesAdapter(lifecycleScope)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = (activity as IGetViewModelList).getViewModel()
        mViewModelLiked = (activity as IGetLikedViewModel).getLikedViewModel()

        setupRecycleView()
        addListeners()


    }

    protected open fun addListeners() {

        recycleViewModel.getItemsRecycle().observe(viewLifecycleOwner){ list->
            imagesAdapter.differ.submitList(list)
        }

        recycleViewModel.setOnUpdateItem { item->
            imagesAdapter.updateImage(item as ItemRecycle.RecycleWallpaperItem)
        }

        mViewModel.getImageToPickUp().observe(viewLifecycleOwner){image ->
            if(image != null){
                val bundle = Bundle()
                bundle.putParcelable(SelectWallpaperActivity.WALLPAPER_TAG, image)
                val intent = Intent(context, SelectWallpaperActivity::class.java)
                intent.putExtras(bundle)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mViewModel.PickUpImage(null)
                selectWallpaperLauncher?.launch(intent)
            }

        }

        addSelectWallpaperListener()

    }

    private var selectWallpaperLauncher : ActivityResultLauncher<Intent>? = null
    protected fun addSelectWallpaperListener(){
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

    protected open fun setupRecycleView() {

        val recycle = view?.findViewById<RecyclerView>(R.id.rcViewAdapter)
        recycle?.let {

            val gridManager = GridLayoutManager(activity,2)
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