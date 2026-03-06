package com.vladan.offlinefirstarch.feature.details.data.remote

import com.vladan.offlinefirstarch.core.network.FoursquareApi
import com.vladan.offlinefirstarch.core.network.dto.PlaceDetailsDto
import javax.inject.Inject

class DetailsRemoteDataSource @Inject constructor(
    private val api: FoursquareApi,
) {
    suspend fun getPlaceDetails(fsqId: String): PlaceDetailsDto {
        return api.getPlaceDetails(fsqId)
    }
}
