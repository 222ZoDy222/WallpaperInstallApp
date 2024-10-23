package com.zdy.wallpaperinstallapp.WallpapersList.WebList.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.zdy.wallpaperinstallapp.WallpapersList.UI.FragmentList
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.UI.RecycleView.ItemRecycle
import com.zdy.wallpaperinstallapp.databinding.FragmentListBinding
import com.zdy.wallpaperinstallapp.utils.Resource


class ListFragmentWeb : FragmentList() {


    lateinit var binding : FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }




    override fun setupRecycleView(){
        super.setupRecycleView()

        imagesAdapter.setOnRefreshClickListener {
            mViewModel.getRandomImages()
        }


    }

    override fun addListeners(){

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