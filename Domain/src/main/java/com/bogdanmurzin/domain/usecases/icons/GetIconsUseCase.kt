package com.epam.elearn.domain.usecases.icons

import com.bogdanmurzin.domain.entities.Icon
import com.bogdanmurzin.domain.repositories.IconRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetIconsUseCase @Inject constructor(private val iconRepository: IconRepository) {

    operator fun invoke(query: String): Observable<Result<List<Icon>>> =
        iconRepository.getRemoteIcons(query)
}