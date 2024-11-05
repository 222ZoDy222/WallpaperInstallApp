package com.zdy.wallpaperinstallapp.wallpapersList.WebList.ViewModel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
    private val application: Application,
    val imageRepository: ImagesRepository) : AndroidViewModel(application) {



    private val errorMessage : MutableLiveData<ErrorType> = MutableLiveData(null)
    fun getErrorMessage() = errorMessage


    private val imageRequest: MutableLiveData<Resource<RequestImages>> = MutableLiveData()
    fun getImageRequest(): MutableLiveData<Resource<RequestImages>> = imageRequest



    init {

        getRandomImages()

    }

    fun getRandomImages() = viewModelScope.launch {

        imageRequest.postValue(Resource.Loading())
        errorMessage.postValue(null)
        try{
            if(ImagesRepository.hasInternetConnection(application.applicationContext)){
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

    private val imageToPickUp = MutableLiveData<PickUpImage>()
    fun getImageToPickUp() : MutableLiveData<PickUpImage> = imageToPickUp

    fun PickUpImage(pickImage: PickUpImage?){
        if(pickImage != null){
            SaveImage(pickImage)
        }
        imageToPickUp.value = pickImage
    }

    private fun SaveImage(pickImage: PickUpImage){

        pickImage.bitmap?.let {
            saveBitmapToFile(it)
        }

    }

    private fun saveBitmapToFile(bitmap: Bitmap): String? {
        try {
            val file = File(application.cacheDir, SELECTED_IMAGE_NAME)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
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