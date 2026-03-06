package com.vladan.offlinefirstarch.feature.search.domain.repository

import com.vladan.offlinefirstarch.core.common.Resource
import com.vladan.offlinefirstarch.feature.search.domain.model.Venue
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchVenues(query: String, latLng: String): Flow<Resource<List<Venue>>>
}
