package com.vladan.holycodetask.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialMediaDto(
    @SerialName("facebook_id") val facebookId: String? = null,
    @SerialName("instagram") val instagram: String? = null,
    @SerialName("twitter") val twitter: String? = null,
)
