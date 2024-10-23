package com.zdy.wallpaperinstallapp.models.Web.SSL

import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import java.io.InputStream

@GlideModule
class UnsafeGlideModule : AppGlideModule() {
    override fun registerComponents(
        context: android.content.Context,
        glide: Glide,
        registry: Registry
    ) {
        val client = UnSafeSSL.createUnSafeOkHttpClient()
        registry.replace(
            GlideUrl::class.java, InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }
}