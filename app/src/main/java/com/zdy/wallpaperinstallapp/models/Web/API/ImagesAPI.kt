package com.zdy.wallpaperinstallapp.models.Web.API

import com.zdy.wallpaperinstallapp.models.Web.ObjectsWeb.RequestImages
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesAPI {


    @GET("/get_images")
    suspend fun GetRandomImages(
        @Query("limit")
        limit :String = "31",

    ) : Response<RequestImages>
}