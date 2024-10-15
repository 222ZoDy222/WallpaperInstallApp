package com.zdy.wallpaperinstallapp.Web.API

import com.zdy.wallpaperinstallapp.Web.Objects.requestImages
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Tag

interface ImagesAPI {

    // https://api.nekosapi.com/v3/images/random
    @GET("/v3/images/random")
    suspend fun GetRandomImages(
        @Query("limit")
        limit :String = "5",
        @Query("rating")
        rating :String = "safe",
        @Query("tag")
        tag :String = "2"

    ) : Response<requestImages>
}