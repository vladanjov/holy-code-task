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
        @Query("radius") radius: Int = 5000,
        @Query("limit") limit: Int = 20,
    ): PlaceSearchResponse

    @GET("places/{fsq_place_id}")
    suspend fun getPlaceDetails(
        @Path("fsq_place_id") fsqPlaceId: String,
    ): PlaceDetailsDto
}
