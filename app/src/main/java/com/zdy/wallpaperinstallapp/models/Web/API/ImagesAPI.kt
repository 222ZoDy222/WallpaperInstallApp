package com.zdy.wallpaperinstallapp.models.Web.API

import com.zdy.wallpaperinstallapp.models.ObjectsWeb.RequestImages
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ImagesAPI {

    // https://api.nekosapi.com/v3/images/random
    @GET("/get_images")
    suspend fun GetRandomImages(
        @Query("limit")
        limit :String = "31",

    ) : Response<RequestImages>
}