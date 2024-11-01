package com.zdy.wallpaperinstallapp.models.Web.SSL

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class UnSafeSSL {
    companion object{

        fun createSaveOkHttpClient(): OkHttpClient {

            return OkHttpClient.Builder()
                .addInterceptor(getLogging())
                .build()

        }

        private fun getLogging():HttpLoggingInterceptor{
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return logging
        }

        fun createUnSafeOkHttpClient(): OkHttpClient {
            return try {



                // Server has no domain, so we should accept all SSL cert
                val trustAllCerts: Array<TrustManager> = arrayOf(X509TrustAllManager())
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // ----------------------------------------------------------------------//

                OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.socketFactory,trustAllCerts[0] as X509TrustManager)
                    .addInterceptor(getLogging())
                    .hostnameVerifier { _, _ -> true }
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .build()

            } catch (e: Exception) {
                throw RuntimeException(e)
                // TODO: java.net.SocketTimeoutException: timeout
            }
        }

    }

    class X509TrustAllManager : X509TrustManager {

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