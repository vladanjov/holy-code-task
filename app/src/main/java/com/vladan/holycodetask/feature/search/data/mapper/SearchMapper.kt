package com.vladan.holycodetask.feature.search.data.mapper

import com.vladan.holycodetask.core.database.entity.VenueEntity
import com.vladan.holycodetask.core.network.dto.PlaceDto
import com.vladan.holycodetask.feature.search.domain.model.Venue
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun PlaceDto.toEntity(): VenueEntity = VenueEntity(
    fsqId = fsqPlaceId,
    name = name,
    categoryName = categories.firstOrNull()?.name,
    categoryIconUrl = categories.firstOrNull()?.icon?.let { "${it.prefix}64${it.suffix}" },
    address = location?.address,
    formattedAddress = location?.formattedAddress,
    locality = location?.locality,
    distance = distance,
    latitude = latitude,
    longitude = longitude,
    description = description,
    tel = tel,
    website = website,
    hoursDisplay = hours?.display,
    openNow = hours?.openNow,
    rating = rating,
    price = price,
    photosJson = if (photos.isNotEmpty()) json.encodeToString(photos) else null,
    socialFacebookId = socialMedia?.facebookId,
    socialInstagram = socialMedia?.instagram,
    socialTwitter = socialMedia?.twitter,
)

fun VenueEntity.toVenue(): Venue = Venue(
    fsqId = fsqId,
    name = name,
    categoryName = categoryName,
    categoryIconUrl = categoryIconUrl,
    address = formattedAddress ?: address,
    distance = distance,
)
