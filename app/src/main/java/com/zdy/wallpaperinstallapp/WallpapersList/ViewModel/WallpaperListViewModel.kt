package com.zdy.wallpaperinstallapp.WallpapersList.ViewModel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.PickUpWallpaper.Objects.PickUpImage
import com.zdy.wallpaperinstallapp.Web.Objects.RequestImages
import com.zdy.wallpaperinstallapp.Web.Requests.ImageRepository
import com.zdy.wallpaperinstallapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class WallpaperListViewModel(
    private val application: Application,
    val imageRepository: ImageRepository) : AndroidViewModel(application) {


    private val imageRequest: MutableLiveData<Resource<RequestImages>> = MutableLiveData()
    fun getImageRequest(): MutableLiveData<Resource<RequestImages>> = imageRequest

    init {

        getRandomImages()

        // TODO: Start Load ROOM wallpaper

    }

    fun getRandomImages() = viewModelScope.launch {

        imageRequest.postValue(Resource.Loading())
        try{
            val response = imageRepository.getImages()
            imageRequest.postValue(handleImageResponse(response))

        } catch (ex: Exception){
            // TODO: плохое соединение с интернетом / повторить попытку
            imageRequest.postValue(null)
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

    fun PickUpImage(pickImage: PickUpImage){
        SaveImage(pickImage)
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


    fun ConvertImages(requestImages: RequestImages): List<PickUpImage> {

        val resultList = mutableListOf<PickUpImage>()
        for (image in requestImages.items) {
            var description = ""
            try {
                description = image.tags.toString()
            } catch (ex: Exception) {

            }
            resultList.add(PickUpImage(null, image.image_url, description))
        }

        return resultList

    }


    companion object{

        const val SELECTED_IMAGE_NAME = "SelectedImage.wi"

    }


}