package com.zdy.wallpaperinstallapp.models.Web.API

import com.zdy.wallpaperinstallapp.models.ObjectsWeb.RequestImages
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesAPI {

    // https://api.nekosapi.com/v3/images/random
    @GET("/v3/images/random")
    suspend fun GetRandomImages(
        @Query("limit")
        limit :String = "5",
        @Query("rating")
        rating :String = "safe",
        @Query("id")
        id :String = "20",
        @Query("tag")
        tag: String = "9",


    ) : Response<RequestImages>
}