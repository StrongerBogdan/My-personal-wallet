package com.epam.elearn.data.mappers.icons


import com.bogdanmurzin.data.api.icons.IconsApiResponse
import com.bogdanmurzin.domain.entities.Icon
import javax.inject.Inject

class IconsApiResponseMapper @Inject constructor() {
    fun toIconList(
        response: IconsApiResponse
    ): List<Icon> {
        return response.icons.map {
            val rasterSize = it.rasterSizes.size
            Icon(
                it.iconId,
                it.rasterSizes[rasterSize - 1].formats[0].previewUrl,
                it.rasterSizes[rasterSize - 1].formats[0].downloadUrl,
                it.isPremium
            )
        }
    }
}