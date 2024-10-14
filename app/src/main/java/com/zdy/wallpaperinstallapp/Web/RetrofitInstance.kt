package com.zdy.wallpaperinstallapp.Web

import com.zdy.wallpaperinstallapp.Web.API.ImagesAPI
import com.zdy.wallpaperinstallapp.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RetrofitInstance {

    companion object{

        private val retrofit by lazy{
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .build()
        }


        val api by lazy{
            retrofit.create(ImagesAPI::class.java)
        }

    }

}