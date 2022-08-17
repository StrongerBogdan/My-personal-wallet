package com.bogdanmurzin.data.repositories.icons

import android.content.Context
import com.bogdanmurzin.data.R
import com.bogdanmurzin.data.api.icons.RetrofitIconsService
import com.bogdanmurzin.domain.entities.Icon
import com.epam.elearn.data.mappers.icons.IconsApiResponseMapper
import com.epam.elearn.data.repositories.icons.IconsRemoteDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class IconsRemoteDataSourceImpl @Inject constructor(
    private val service: RetrofitIconsService,
    private val mapper: IconsApiResponseMapper,
    @ApplicationContext private val context: Context
) : IconsRemoteDataSource {

    override fun getRemoteIcons(query: String): Observable<Result<List<Icon>>> {
        val iconsObservable = service.getIcons(query)
        return iconsObservable.flatMap { result ->
            if (result.totalCount == 0) {
                Observable.just(Result.failure(IllegalStateException(context.getString(R.string.nothing_found_for_this_query))))
            } else {
                Observable.just(Result.success(mapper.toIconList(result)))
            }
        }
    }
}