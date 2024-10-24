package com.zdy.wallpaperinstallapp.utils

class Constants {
    companion object{

        const val USE_OUR_SERVER = true

        const val API_KEY = "8U3mnA2a8AnMY5xnZuIGzw==OWkmCrenYUABAoJL"

        const val BASE_URL_API_NINJAS = "https://api.api-ninjas.com"
        const val BASE_URL_NEKO_IMAGE = "https://api.nekosapi.com"
        const val OUR_URL = "https://193.227.241.94:5000"


        const val NEKO_IMAGE_GET_REQUEST = "v3/images/random"
        const val OUR_SERVER_GET_REQUEST = "/get_images"


        val BASE_URL = if(USE_OUR_SERVER) OUR_URL else BASE_URL_NEKO_IMAGE
    }
}