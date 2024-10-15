package com.zdy.wallpaperinstallapp.Web.Requests

import com.zdy.wallpaperinstallapp.Web.RetrofitInstance

class ImageRepository {

    suspend fun getImages(limit: Int = 30) = RetrofitInstance.api.GetRandomImages(limit.toString())

}