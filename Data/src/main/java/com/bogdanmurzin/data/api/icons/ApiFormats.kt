package com.bogdanmurzin.data.api.icons

import com.google.gson.annotations.SerializedName

data class ApiFormats(
    @SerializedName("format") val format: String,
    @SerializedName("preview_url") val previewUrl: String,
    @SerializedName("download_url") val downloadUrl: String
)