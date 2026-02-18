package com.vladan.holycodetask.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    @SerialName("address") val address: String? = null,
    @SerialName("cross_street") val crossStreet: String? = null,
    @SerialName("locality") val locality: String? = null,
    @SerialName("region") val region: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("formatted_address") val formattedAddress: String? = null,
    @SerialName("postcode") val postcode: String? = null,
)
