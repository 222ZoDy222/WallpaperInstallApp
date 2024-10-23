package com.zdy.wallpaperinstallapp.models.Web

import com.zdy.wallpaperinstallapp.models.Web.API.ImagesAPI
import com.zdy.wallpaperinstallapp.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.net.ssl.SSLSession;



class RetrofitInstance {

    companion object{

        private val retrofit by lazy{
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            // TODO: java.net.SocketTimeoutException: timeout
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build()
        }


        val api by lazy{
            retrofit.create(ImagesAPI::class.java)
        }


        private fun createOkHttpClient(): OkHttpClient {
            return try {
                val trustAllCerts: Array<TrustManager> = arrayOf(MyManager())
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(),
                        trustAllCerts[0] as X509TrustManager
                    )
                    .addInterceptor(logging)
                    .hostnameVerifier { hostname: String?, session: SSLSession? -> true }
                    .build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }


    }

    class MyManager : X509TrustManager {

        override fun checkServerTrusted(
            p0: Array<out java.security.cert.X509Certificate>?,
            p1: String?
        ) {
            //allow all
        }

        override fun checkClientTrusted(
            p0: Array<out java.security.cert.X509Certificate>?,
            p1: String?
        ) {
            //allow all
        }

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return arrayOf()
        }
    }

}