package com.zdy.wallpaperinstallapp.hilt

import com.zdy.wallpaperinstallapp.db.WallpaperDatabase
import com.zdy.wallpaperinstallapp.repository.ImagesRepository
import com.zdy.wallpaperinstallapp.api.ImagesAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(wallpaperDatabase: WallpaperDatabase, api: ImagesAPI) : ImagesRepository {
        return ImagesRepository(wallpaperDatabase, api)
    }

}