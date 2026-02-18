package com.vladan.holycodetask.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("fsq_category_id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("short_name") val shortName: String? = null,
    @SerialName("plural_name") val pluralName: String? = null,
    @SerialName("icon") val icon: IconDto? = null,
)

@Serializable
data class IconDto(
    @SerialName("prefix") val prefix: String,
    @SerialName("suffix") val suffix: String,
)
