package com.zdy.wallpaperinstallapp.models.Web


import com.zdy.wallpaperinstallapp.models.Web.API.ImagesAPI
import com.zdy.wallpaperinstallapp.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class RetrofitInstance {

    companion object{

        private val retrofit by lazy{
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)



            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createSaveOkHttpClient())
                .build()
        }


        val api by lazy{
            retrofit.create(ImagesAPI::class.java)
        }



        private fun getLogging():HttpLoggingInterceptor{
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return logging
        }
        fun createSaveOkHttpClient(): OkHttpClient {

            return OkHttpClient.Builder()
                .addInterceptor(getLogging())
                .build()

        }



    }



}