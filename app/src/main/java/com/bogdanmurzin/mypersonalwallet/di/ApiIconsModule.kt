package com.bogdanmurzin.mypersonalwallet.di

import com.bogdanmurzin.data.api.icons.RetrofitIconsService
import com.bogdanmurzin.mypersonalwallet.common.Constants.RETROFIT_ICONS_NAMED
import com.bogdanmurzin.mypersonalwallet.network.utils.NetworkConstants
import com.bogdanmurzin.mypersonalwallet.network.utils.NetworkConstants.APY_KEY_ICONS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
class ApiIconsModule {

    @Provides
    @Named(RETROFIT_ICONS_NAMED)
    fun provideRetrofitIcons(
        @Named(RETROFIT_ICONS_NAMED) client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        rxJavaCallAdapterFactory: RxJava3CallAdapterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.BASE_URL_ICONS)
        .client(client)
        .addCallAdapterFactory(rxJavaCallAdapterFactory)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Provides
    fun provideRetrofitIconsService(@Named(RETROFIT_ICONS_NAMED) retrofit: Retrofit)
            : RetrofitIconsService =
        retrofit.create(RetrofitIconsService::class.java)

    @Provides
    @Named(RETROFIT_ICONS_NAMED)
    fun provideOkHttpClient(
        @Named(RETROFIT_ICONS_NAMED) authInterceptor: Interceptor,
        logging: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    @Named(RETROFIT_ICONS_NAMED)
    fun provideAuthInterceptor(): Interceptor =
        object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .addHeader(NetworkConstants.HEADER_AUTH_ICONS, APY_KEY_ICONS).build()
                return chain.proceed(request)
            }
        }
}