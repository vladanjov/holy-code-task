package com.vladan.holycodetask.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HoursDto(
    @SerialName("display") val display: String? = null,
    @SerialName("open_now") val openNow: Boolean? = null,
    @SerialName("regular") val regular: List<RegularHoursDto> = emptyList(),
)

@Serializable
data class RegularHoursDto(
    @SerialName("day") val day: Int,
    @SerialName("open") val open: String,
    @SerialName("close") val close: String,
)
