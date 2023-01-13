package com.bogdanmurzin.mypersonalwallet.di

import com.bogdanmurzin.mypersonalwallet.ui.presenter.IconsPresenter
import com.bogdanmurzin.domain.usecases.icons.GetIconsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class IconsActivityModule {

    @Provides
    fun provideIconsPresenter(useCse: GetIconsUseCase): IconsPresenter =
        IconsPresenter(useCse)

}