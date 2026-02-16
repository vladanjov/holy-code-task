package com.vladan.holycodetask.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    @SerialName("id") val id: String,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("prefix") val prefix: String,
    @SerialName("suffix") val suffix: String,
    @SerialName("width") val width: Int? = null,
    @SerialName("height") val height: Int? = null,
) {
    fun toUrl(size: String = "original"): String = "${prefix}${size}${suffix}"
}
