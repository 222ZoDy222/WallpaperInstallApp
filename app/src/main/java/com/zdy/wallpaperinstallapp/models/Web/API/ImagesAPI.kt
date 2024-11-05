package com.zdy.wallpaperinstallapp.models.Web.API

import com.zdy.wallpaperinstallapp.models.Web.ObjectsWeb.RequestImages
import com.zdy.wallpaperinstallapp.utils.Constants.Companion.NEKO_IMAGE_GET_REQUEST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesAPI {


    @GET(NEKO_IMAGE_GET_REQUEST)
    suspend fun GetRandomImages(
        @Query("limit")
        limit :String = "31",
        @Query("rating")
        rating: String = "safe"

    ) : Response<RequestImages>
}