package com.vladan.holycodetask.feature.search.data.remote

import com.vladan.holycodetask.core.network.FoursquareApi
import com.vladan.holycodetask.core.network.dto.PlaceDto
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(
    private val api: FoursquareApi,
) {
    suspend fun searchPlaces(query: String, latLng: String): List<PlaceDto> {
        return api.searchPlaces(query = query, latLng = latLng).results
    }
}
