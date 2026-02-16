package com.vladan.holycodetask.feature.search.domain.repository

import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.feature.search.domain.model.Venue
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchVenues(query: String, latLng: String): Flow<Resource<List<Venue>>>
}
