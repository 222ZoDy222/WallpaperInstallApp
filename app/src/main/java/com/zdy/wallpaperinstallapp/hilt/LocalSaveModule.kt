package com.zdy.wallpaperinstallapp.hilt

import com.zdy.wallpaperinstallapp.models.localSave.LocalSaveModel
import com.zdy.wallpaperinstallapp.repository.ImagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class LocalSaveModule {
    @Provides
    @ActivityRetainedScoped
    fun provideLocalSaveModule(repository: ImagesRepository) : LocalSaveModel{
        return LocalSaveModel(repository)
    }

}