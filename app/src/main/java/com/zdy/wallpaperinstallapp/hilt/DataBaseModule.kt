package com.zdy.wallpaperinstallapp.hilt

import android.app.Application
import androidx.room.Room
import com.zdy.wallpaperinstallapp.db.WallpaperDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    @Singleton
    fun provideDB(app: Application) : WallpaperDatabase {
        return Room.databaseBuilder(
            app,
            WallpaperDatabase::class.java,
            WallpaperDatabase.DATABASE_NAME
        ).build()
    }

}