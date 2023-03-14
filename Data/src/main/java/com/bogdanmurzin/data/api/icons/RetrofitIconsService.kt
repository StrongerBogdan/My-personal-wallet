package com.bogdanmurzin.data.api.icons

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitIconsService {

    @GET("search")
    fun getIcons(@Query("query") query: String, @Query("count") count: Int = 60)
            : Observable<IconsApiResponse>
}