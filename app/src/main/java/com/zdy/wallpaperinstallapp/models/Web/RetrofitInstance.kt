package com.zdy.wallpaperinstallapp.models.Web

import android.annotation.SuppressLint
import com.zdy.wallpaperinstallapp.models.Web.API.ImagesAPI
import com.zdy.wallpaperinstallapp.models.Web.SSL.UnSafeSSL
import com.zdy.wallpaperinstallapp.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.net.ssl.SSLSession;



class RetrofitInstance {

    companion object{

        private val retrofit by lazy{
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)



            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(UnSafeSSL.createUnSafeOkHttpClient())
                .build()
        }


        val api by lazy{
            retrofit.create(ImagesAPI::class.java)
        }




    }



}