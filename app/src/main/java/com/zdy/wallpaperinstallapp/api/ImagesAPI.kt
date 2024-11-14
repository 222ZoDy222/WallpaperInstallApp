package com.zdy.wallpaperinstallapp.api

import com.zdy.wallpaperinstallapp.api.objectsWeb.RequestImages
import com.zdy.wallpaperinstallapp.utils.Constants.Companion.GET_REQUEST_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesAPI {


    @GET(GET_REQUEST_URL)
    suspend fun GetRandomImages(
        @Query("limit")
        limit :String = "31",
        @Query("rating")
        rating: String = "safe"

    ) : Response<RequestImages>
}