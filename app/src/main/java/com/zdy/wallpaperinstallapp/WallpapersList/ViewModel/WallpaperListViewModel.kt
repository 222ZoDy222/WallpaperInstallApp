package com.zdy.wallpaperinstallapp.WallpapersList.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.Web.Objects.NekoImage
import com.zdy.wallpaperinstallapp.Web.Objects.RequestImages
import com.zdy.wallpaperinstallapp.Web.Requests.ImageRepository
import com.zdy.wallpaperinstallapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class WallpaperListViewModel(
    val imageRepository: ImageRepository) : ViewModel(){



    private val imageRequest : MutableLiveData<Resource<RequestImages>> = MutableLiveData()
    fun getImageRequest() : MutableLiveData<Resource<RequestImages>> = imageRequest

    init {

        getRandomImages()

        // TODO: Start Load ROOM wallpaper

    }

    fun getRandomImages() = viewModelScope.launch {

        imageRequest.postValue(Resource.Loading())
        val response = imageRepository.getImages()
        imageRequest.postValue(handleImageResponse(response))

    }

    private fun handleImageResponse(response: Response<RequestImages>) : Resource<RequestImages>{

        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())

    }








}