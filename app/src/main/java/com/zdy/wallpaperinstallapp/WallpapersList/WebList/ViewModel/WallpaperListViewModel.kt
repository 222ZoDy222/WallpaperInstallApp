package com.zdy.wallpaperinstallapp.WallpapersList.WebList.ViewModel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.WallpapersList.WebList.UI.RecycleView.ItemRecycle
import com.zdy.wallpaperinstallapp.models.ObjectsWeb.RequestImages
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


    private val imageRequest: MutableLiveData<Resource<RequestImages>> = MutableLiveData()
    fun getImageRequest(): MutableLiveData<Resource<RequestImages>> = imageRequest


    private val listWallpaperItems : MutableLiveData<MutableList<ItemRecycle>> = MutableLiveData(
        mutableListOf<ItemRecycle>()
    )

    fun getListWallpaperItems() = listWallpaperItems

    init {

        getRandomImages()

    }

    fun getRandomImages() = viewModelScope.launch {

        imageRequest.postValue(Resource.Loading())
        try{
            val response = imageRepository.getRandomImages()
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