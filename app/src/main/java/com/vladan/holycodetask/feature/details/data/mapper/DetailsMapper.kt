package com.vladan.holycodetask.feature.details.data.mapper

import com.vladan.holycodetask.core.database.entity.VenueEntity
import com.vladan.holycodetask.core.network.dto.PhotoDto
import com.vladan.holycodetask.core.network.dto.PlaceDetailsDto
import com.vladan.holycodetask.feature.details.domain.model.VenueDetails
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

private const val MOCK_DESCRIPTION =
    "A popular local spot known for its welcoming atmosphere and great service. " +
        "Whether you're visiting for the first time or you're a regular, " +
        "there's always something to enjoy here."

private const val MOCK_HOURS_DISPLAY = "Mon-Fri 9:00 AM - 10:00 PM, Sat-Sun 10:00 AM - 11:00 PM"

private val MOCK_PHOTO_URLS = listOf(
    "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?fm=jpg&q=60&w=3000&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8cmVzdGF1cmFudHxlbnwwfHwwfHx8MA%3D%3D",
    "https://archisphere.at/wp-content/uploads/2023/06/vienna-restaurant-interior-design-with-integrated-shelves-by-archisphere-in-vienna-photo-copyright-christof-wagner-1024x768.jpg",
    "https://res.cloudinary.com/nmg-prod/image/upload/f_auto/q_auto:good/v1723234528/bg_content/Landing%20Pages/Restaurants%20-%20cat652611/Restaurants-LP_01-Restaurant_080924.jpg",
)

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
        description = description ?: MOCK_DESCRIPTION,
        tel = tel.orEmpty(),
        website = website.orEmpty(),
        hoursDisplay = hoursDisplay ?: MOCK_HOURS_DISPLAY,
        openNow = openNow,
        photos = photoUrls.ifEmpty { MOCK_PHOTO_URLS },
        latitude = latitude,
        longitude = longitude,
        socialInstagram = socialInstagram.orEmpty(),
        socialTwitter = socialTwitter.orEmpty(),
    )
}
