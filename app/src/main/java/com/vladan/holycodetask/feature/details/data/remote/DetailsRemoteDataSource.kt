package com.vladan.holycodetask.feature.details.data.remote

import com.vladan.holycodetask.core.network.FoursquareApi
import com.vladan.holycodetask.core.network.dto.PlaceDetailsDto
import javax.inject.Inject

class DetailsRemoteDataSource @Inject constructor(
    private val api: FoursquareApi,
) {
    suspend fun getPlaceDetails(fsqId: String): PlaceDetailsDto {
        return api.getPlaceDetails(fsqId)
    }
}
