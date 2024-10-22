package com.zdy.wallpaperinstallapp.WallpapersList.UI

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.WallpapersList.LikedList.Interfaces.IGetLikedViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.LikedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.Interfaces.IGetViewModelList
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.UI.RecycleView.ImagesAdapter
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.ViewModel.WallpaperListViewModel

open class FragmentList : Fragment() {

    protected lateinit var mViewModel: WallpaperListViewModel
    protected  lateinit var mViewModelLiked: WallpaperLikedListViewModel

    val imagesAdapter: ImagesAdapter by lazy {
        ImagesAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = (activity as IGetViewModelList).getViewModel()
        mViewModelLiked = (activity as IGetLikedViewModel).getLikedViewModel()

        setupRecycleView()
        addListeners()


    }

    protected open fun addListeners() {

    }

    protected open fun setupRecycleView() {

        val recycle = view?.findViewById<RecyclerView>(R.id.rcViewAdapter)
        recycle?.let {
            it.adapter = imagesAdapter
            it.layoutManager = GridLayoutManager(activity,2)
        }

        imagesAdapter.setOnItemClickListener { image->
            mViewModel.PickUpImage(image)
        }

        imagesAdapter.setOnItemLikeClickListener {
            var result = mViewModelLiked.onLikeClicked(it)
            imagesAdapter.updateImageSavedStatus(it)
            view?.let{view->
                Snackbar.make(view,result, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}