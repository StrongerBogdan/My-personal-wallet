package com.epam.elearn.data.repositories.icons


import com.bogdanmurzin.domain.entities.Icon
import com.bogdanmurzin.domain.repositories.IconRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class IconsRepositoryImpl @Inject constructor(
    private val remoteDataSource: IconsRemoteDataSource
) : IconRepository {

    override fun getRemoteIcons(query: String): Observable<Result<List<Icon>>> =
        remoteDataSource.getRemoteIcons(query)
}