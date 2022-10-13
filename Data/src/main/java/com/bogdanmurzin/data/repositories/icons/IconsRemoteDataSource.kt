package com.epam.elearn.data.repositories.icons


import com.bogdanmurzin.domain.entities.Icon
import io.reactivex.rxjava3.core.Observable

interface IconsRemoteDataSource {
    fun getRemoteIcons(query: String): Observable<Result<List<Icon>>>
}