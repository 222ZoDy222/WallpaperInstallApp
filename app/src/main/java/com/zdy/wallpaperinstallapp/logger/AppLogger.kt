package com.zdy.wallpaperinstallapp.logger

class AppLogger {

    companion object{

        const val TAG = "Wallpaper_Log"

        fun Log(msg: String){

            android.util.Log.i(TAG,msg)

        }

    }

}