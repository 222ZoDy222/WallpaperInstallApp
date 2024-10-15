package com.zdy.wallpaperinstallapp.WallpapersList.UI

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.IGetViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.INavigate
import com.zdy.wallpaperinstallapp.WallpapersList.UI.RecycleView.ImagesAdapter
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListViewModel
import com.zdy.wallpaperinstallapp.databinding.FragmentListBinding
import com.zdy.wallpaperinstallapp.utils.Resource


class ListFragment : Fragment() {

    private lateinit var mViewModel: WallpaperListViewModel
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
        mViewModel = (activity as IGetViewModel).getViewModel()
        setupRecycleView()
        addListeners()




    }

    lateinit var imagesAdapter: ImagesAdapter

    private fun setupRecycleView(){

        binding.apply {
            imagesAdapter = ImagesAdapter()

            imagesAdapter.setOnItemClickListener { image->



            }

            rcViewAdapter.apply {
                adapter = imagesAdapter
                layoutManager = GridLayoutManager(activity,2)
            }

        }

    }

    private fun addListeners(){

        mViewModel.getImageRequest().observe(viewLifecycleOwner){response->
            when(response){
                is Resource.Success ->{
                    Loading(false)
                    response.data?.let {
                        imagesAdapter.differ.submitList(it.items)
                    }
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

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()

    }
}