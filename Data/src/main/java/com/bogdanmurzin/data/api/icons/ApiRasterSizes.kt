package com.bogdanmurzin.data.api.icons

import com.google.gson.annotations.SerializedName

data class ApiRasterSizes(
    @SerializedName("formats") val formats: ArrayList<ApiFormats>
)