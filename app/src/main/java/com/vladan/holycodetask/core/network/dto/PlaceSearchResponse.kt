package com.vladan.holycodetask.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceSearchResponse(
    @SerialName("results") val results: List<PlaceDto> = emptyList(),
)
