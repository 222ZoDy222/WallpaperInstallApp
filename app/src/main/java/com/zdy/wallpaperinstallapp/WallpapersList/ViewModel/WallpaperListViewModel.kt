package com.zdy.wallpaperinstallapp.WallpapersList.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.Web.Objects.Item
import com.zdy.wallpaperinstallapp.Web.Objects.nekoImage
import com.zdy.wallpaperinstallapp.Web.Requests.ImageRepository
import com.zdy.wallpaperinstallapp.Web.RetrofitInstance
import com.zdy.wallpaperinstallapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class WallpaperListViewModel(
    val imageRepository: ImageRepository) : ViewModel(){



    val imageRequest : MutableLiveData<Resource<nekoImage>> = MutableLiveData()

    init {

        // TODO: Start Load Internet Wallpaper

        getRandomImage()

        // TODO: Start Load ROOM wallpaper

    }

    fun getRandomImage() = viewModelScope.launch {

        imageRequest.postValue(Resource.Loading())
        val response = imageRepository.getImages()
        imageRequest.postValue(handleImageResponse(response))

    }

    private fun handleImageResponse(response: Response<nekoImage>) : Resource<nekoImage>{

        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())

    }







}