package com.zdy.wallpaperinstallapp.Web.Repository

import com.zdy.wallpaperinstallapp.Web.RetrofitInstance

class ImagesRepository {


    suspend fun getRandomImages() = RetrofitInstance.api.GetRandomImages()


}