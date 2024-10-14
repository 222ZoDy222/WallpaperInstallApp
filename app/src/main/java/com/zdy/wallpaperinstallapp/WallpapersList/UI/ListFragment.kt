package com.zdy.wallpaperinstallapp.WallpapersList.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProvider
import com.zdy.wallpaperinstallapp.MainActivity
import com.zdy.wallpaperinstallapp.R
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.IGetViewModel
import com.zdy.wallpaperinstallapp.WallpapersList.Interfaces.INavigate
import com.zdy.wallpaperinstallapp.WallpapersList.ViewModel.WallpaperListFactory
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
        addListeners()




    }



    private fun addListeners(){
        binding.button.setOnClickListener {
            (requireActivity() as INavigate).NavigateToLikedList()
        }
        mViewModel.imageRequest.observe(viewLifecycleOwner){response->
            when(response){
                is Resource.Success ->{
                    binding.resultResponseTextView.text = "Success"
                }
                is Resource.Error ->{
                    binding.resultResponseTextView.text = response.message
                }
                is Resource.Loading ->{
                    binding.resultResponseTextView.text = "Loading ..."
                }
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()

    }
}