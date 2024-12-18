package com.zdy.wallpaperinstallapp.activity.webList.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.inheritance.ListViewModel
import com.zdy.wallpaperinstallapp.models.localSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.api.objectsWeb.RequestImages
import com.zdy.wallpaperinstallapp.models.web.InternetConnectionModel
import com.zdy.wallpaperinstallapp.repository.ImagesRepository
import com.zdy.wallpaperinstallapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WallpaperListViewModel @Inject constructor(
    private val imageRepository: ImagesRepository, localSaveModel: LocalSaveModel
) : ListViewModel(localSaveModel) {



    private val errorMessage : MutableLiveData<ErrorType> = MutableLiveData(null)
    fun getErrorMessage() = errorMessage


    private val imageRequest: MutableLiveData<Resource<RequestImages>> = MutableLiveData()
    fun getImageRequest(): MutableLiveData<Resource<RequestImages>> = imageRequest


    fun getRandomImages(context: Context) = viewModelScope.launch {

        imageRequest.postValue(Resource.Loading())
        errorMessage.postValue(null)
        try{
            if(InternetConnectionModel.hasInternetConnection(context)){
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




}