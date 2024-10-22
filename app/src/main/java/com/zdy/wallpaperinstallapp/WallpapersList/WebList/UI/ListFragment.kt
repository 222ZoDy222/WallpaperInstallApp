package com.zdy.wallpaperinstallapp.WallpapersList.WebList.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zdy.wallpaperinstallapp.WallpapersList.LikedList.Interfaces.IGetLikedViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.LikedList.ViewModel.WallpaperLikedListViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.Interfaces.IGetViewModelList
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.UI.RecycleView.ImagesAdapter
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.UI.RecycleView.ItemRecycle
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.databinding.FragmentListBinding
import com.zdy.wallpaperinstallapp.utils.Resource


class ListFragment : Fragment() {

    private lateinit var mViewModel: WallpaperListViewModel
    private  lateinit var mViewModelLiked: WallpaperLikedListViewModel
    lateinit var binding : FragmentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = (activity as IGetViewModelList).getViewModel()
        mViewModelLiked = (activity as IGetLikedViewModel).getLikedViewModel()

        setupRecycleView()
        addListeners()




    }

    lateinit var imagesAdapter: ImagesAdapter

    private fun setupRecycleView(){

        binding.apply {
            imagesAdapter = ImagesAdapter()

            imagesAdapter.setOnItemClickListener { image->
                mViewModel.PickUpImage(image)
            }

            rcViewAdapter.apply {
                adapter = imagesAdapter
                layoutManager = GridLayoutManager(activity,2)
            }

            imagesAdapter.setOnRefreshClickListener {
                mViewModel.getRandomImages()

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


    var testIndex = 0
    private fun addListeners(){

        mViewModel.getImageRequest().observe(viewLifecycleOwner){imageRequest ->
            binding.reloadContainer.visibility = if (imageRequest == null) View.VISIBLE else View.GONE
            binding.loadbar.visibility = View.GONE
        }

        binding.reloadButton.setOnClickListener {
            mViewModel.getRandomImages()
        }




        mViewModel.getImageRequest().observe(viewLifecycleOwner){response->
            when(response){
                is Resource.Success ->{
                    response.data?.let {
                        imagesAdapter.differ.submitList(null)
                        val newList = mViewModel.ConvertImages(it)
                        val listItems = mutableListOf<ItemRecycle>()
                        for(i in newList){
                            listItems.add(ItemRecycle.RecycleWallpaperItem(i))
                        }
                        listItems.add(ItemRecycle.RecycleButtonItem())
                        imagesAdapter.differ.submitList(listItems)
                        // Update already Liked images
                        listItems.forEach {item->
                            if(item is ItemRecycle.RecycleWallpaperItem){
                                item.image.url?.let { url ->
                                    mViewModelLiked.alreadyHaveWallpaper(url).observe(viewLifecycleOwner){ isSaved->

                                        imagesAdapter.updateImageSavedStatus(item, isSaved)
                                    }
                                }
                            }

                        }
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

    private fun Loading(value: Boolean){
        if(value)
            binding.loadbar.visibility = View.VISIBLE
        else
            binding.loadbar.visibility = View.GONE
    }


}