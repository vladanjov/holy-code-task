package com.vladan.holycodetask.feature.details.data.mapper

import com.vladan.holycodetask.core.database.entity.VenueEntity
import com.vladan.holycodetask.core.network.dto.PhotoDto
import com.vladan.holycodetask.core.network.dto.PlaceDetailsDto
import com.vladan.holycodetask.feature.details.domain.model.VenueDetails
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun PlaceDetailsDto.toEntity(): VenueEntity = VenueEntity(
    fsqId = fsqPlaceId,
    name = name,
    categoryName = categories.firstOrNull()?.name,
    categoryIconUrl = categories.firstOrNull()?.icon?.let { "${it.prefix}64${it.suffix}" },
    address = location?.address,
    formattedAddress = location?.formattedAddress,
    locality = location?.locality,
    distance = null,
    latitude = latitude,
    longitude = longitude,
    description = description,
    tel = tel,
    website = website,
    hoursDisplay = hours?.display,
    openNow = hours?.openNow,
    photosJson = if (photos.isNotEmpty()) json.encodeToString(kotlinx.serialization.builtins.ListSerializer(PhotoDto.serializer()), photos) else null,
    socialFacebookId = socialMedia?.facebookId,
    socialInstagram = socialMedia?.instagram,
    socialTwitter = socialMedia?.twitter,
)

fun VenueEntity.toVenueDetails(): VenueDetails {
    val photoUrls = photosJson?.let {
        try {
            json.decodeFromString<List<PhotoDto>>(it).map { photo -> photo.toUrl("400x300") }
        } catch (_: Exception) {
            emptyList()
        }
    } ?: emptyList()

    return VenueDetails(
        fsqId = fsqId,
        name = name,
        categoryName = categoryName.orEmpty(),
        categoryIconUrl = categoryIconUrl.orEmpty(),
        address = (formattedAddress ?: address).orEmpty(),
        description = description.orEmpty(),
        tel = tel.orEmpty(),
        website = website.orEmpty(),
        hoursDisplay = hoursDisplay.orEmpty(),
        openNow = openNow,
        photos = photoUrls,
        latitude = latitude,
        longitude = longitude,
        socialInstagram = socialInstagram.orEmpty(),
        socialTwitter = socialTwitter.orEmpty(),
    )
}
