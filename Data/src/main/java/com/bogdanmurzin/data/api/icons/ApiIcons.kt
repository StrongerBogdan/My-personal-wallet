package com.bogdanmurzin.data.api.icons

import com.google.gson.annotations.SerializedName

data class ApiIcons(
    @SerializedName("icon_id") val iconId: Int,
    @SerializedName("raster_sizes") val rasterSizes: ArrayList<ApiRasterSizes>,
    @SerializedName("is_premium") val isPremium: Boolean
)