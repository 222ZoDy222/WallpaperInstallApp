package com.zdy.wallpaperinstallapp.di

import com.zdy.wallpaperinstallapp.DB.WallpaperDatabase
import com.zdy.wallpaperinstallapp.models.Repository.ImagesRepository
import dagger.Binds
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
    fun provideRepository(wallpaperDatabase: WallpaperDatabase) : ImagesRepository{
        return ImagesRepository(wallpaperDatabase)
    }

}