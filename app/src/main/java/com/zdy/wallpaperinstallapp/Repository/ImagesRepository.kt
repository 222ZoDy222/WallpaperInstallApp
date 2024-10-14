package com.zdy.wallpaperinstallapp.Repository

import com.zdy.wallpaperinstallapp.Web.RetrofitInstance

class ImagesRepository {


    suspend fun getRandomImages() = RetrofitInstance.api.GetRandomImages()


}