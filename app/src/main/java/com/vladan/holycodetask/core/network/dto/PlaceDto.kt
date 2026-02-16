package com.vladan.holycodetask.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto(
    @SerialName("fsq_place_id") val fsqPlaceId: String,
    @SerialName("name") val name: String,
    @SerialName("location") val location: LocationDto? = null,
    @SerialName("categories") val categories: List<CategoryDto> = emptyList(),
    @SerialName("distance") val distance: Int? = null,
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("tel") val tel: String? = null,
    @SerialName("website") val website: String? = null,
    @SerialName("hours") val hours: HoursDto? = null,
    @SerialName("photos") val photos: List<PhotoDto> = emptyList(),
    @SerialName("social_media") val socialMedia: SocialMediaDto? = null,
    @SerialName("rating") val rating: Double? = null,
    @SerialName("price") val price: Int? = null,
)

typealias PlaceDetailsDto = PlaceDto
