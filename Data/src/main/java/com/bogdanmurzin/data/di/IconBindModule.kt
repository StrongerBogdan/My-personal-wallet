package com.bogdanmurzin.data.di

import com.bogdanmurzin.domain.repositories.IconRepository
import com.epam.elearn.data.repositories.icons.IconsRemoteDataSource
import com.bogdanmurzin.data.repositories.icons.IconsRemoteDataSourceImpl
import com.epam.elearn.data.repositories.icons.IconsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class IconBindModule {
    
    @Binds
    abstract fun bindIconRepository(repo: IconsRepositoryImpl): IconRepository

    @Binds
    abstract fun bindIconsRemoteDataSource(dataSource: IconsRemoteDataSourceImpl): IconsRemoteDataSource
}