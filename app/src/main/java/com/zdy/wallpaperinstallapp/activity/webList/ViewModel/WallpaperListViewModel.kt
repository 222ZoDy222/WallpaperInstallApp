package com.zdy.wallpaperinstallapp.activity.webList.ViewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.inheritance.ListViewModel
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.models.Web.ObjectsWeb.RequestImages
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import com.zdy.wallpaperinstallapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class WallpaperListViewModel(
    context: Context,
    val imageRepository: ImagesRepository) : ListViewModel() {



    private val errorMessage : MutableLiveData<ErrorType> = MutableLiveData(null)
    fun getErrorMessage() = errorMessage


    private val imageRequest: MutableLiveData<Resource<RequestImages>> = MutableLiveData()
    fun getImageRequest(): MutableLiveData<Resource<RequestImages>> = imageRequest



    init {

        getRandomImages(context)

    }

    fun getRandomImages(context: Context) = viewModelScope.launch {

        imageRequest.postValue(Resource.Loading())
        errorMessage.postValue(null)
        try{
            if(ImagesRepository.hasInternetConnection(context)){
                val response = imageRepository.getRandomImages()
                imageRequest.postValue(handleImageResponse(response))
            } else{
                errorMessage.postValue(ErrorType.noInternetConnection)
            }

        } catch (t: Throwable){
            imageRequest.postValue(null)
            when(t){
                is IOException -> errorMessage.postValue(ErrorType.requestError)
                else -> errorMessage.postValue(ErrorType.convertionError)
            }

        }

    }

    private fun handleImageResponse(response: Response<RequestImages>): Resource<RequestImages> {

        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())

    }




    enum class ErrorType{
        noInternetConnection,
        requestError,
        convertionError,

    }


    companion object{

        const val SELECTED_IMAGE_NAME = "SelectedImage.wi"


    }


}