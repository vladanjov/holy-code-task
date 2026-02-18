package com.vladan.holycodetask.core.network

import com.vladan.holycodetask.core.network.dto.PlaceDetailsDto
import com.vladan.holycodetask.core.network.dto.PlaceSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoursquareApi {

    @GET("places/search")
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("ll") latLng: String,
        @Query("radius") radius: Int = DEFAULT_SEARCH_RADIUS,
        @Query("limit") limit: Int = DEFAULT_SEARCH_LIMIT,
    ): PlaceSearchResponse

    @GET("places/{fsq_place_id}")
    suspend fun getPlaceDetails(
        @Path("fsq_place_id") fsqPlaceId: String,
    ): PlaceDetailsDto

    companion object {
        const val DEFAULT_SEARCH_RADIUS = 5000
        const val DEFAULT_SEARCH_LIMIT = 20
    }
}
