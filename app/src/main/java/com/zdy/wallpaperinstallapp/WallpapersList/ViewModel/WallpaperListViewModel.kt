package com.zdy.wallpaperinstallapp.WallpapersList.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.Web.Objects.requestImages
import com.zdy.wallpaperinstallapp.Web.Requests.ImageRepository
import com.zdy.wallpaperinstallapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class WallpaperListViewModel(
    val imageRepository: ImageRepository) : ViewModel(){



    val imageRequest : MutableLiveData<Resource<requestImages>> = MutableLiveData()

    init {

        // TODO: Start Load Internet Wallpaper

        getRandomImages()

        // TODO: Start Load ROOM wallpaper

    }

    fun getRandomImages() = viewModelScope.launch {

        imageRequest.postValue(Resource.Loading())
        val response = imageRepository.getImages()
        imageRequest.postValue(handleImageResponse(response))

    }

    private fun handleImageResponse(response: Response<requestImages>) : Resource<requestImages>{

        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())

    }







}