package com.bogdanmurzin.domain.repositories

import com.bogdanmurzin.domain.entities.Icon
import io.reactivex.rxjava3.core.Observable

interface IconRepository {
    fun getRemoteIcons(query: String): Observable<Result<List<Icon>>>
}