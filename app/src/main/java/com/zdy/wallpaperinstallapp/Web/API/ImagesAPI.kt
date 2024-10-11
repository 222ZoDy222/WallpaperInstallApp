package com.zdy.wallpaperinstallapp.Web.API

import com.zdy.wallpaperinstallapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesAPI {

    @GET("api.unsplash.com/photos")
    suspend fun GetRandomImages(
        @Query("page")
        page :String = "1",
        @Query("client_id")
        client_id :String = Constants.ACCESS_KEY

    )
}