package com.bogdanmurzin.data.api.icons

import com.google.gson.annotations.SerializedName

data class IconsApiResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("icons") val icons: ArrayList<ApiIcons>
)
