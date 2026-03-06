package com.vladan.offlinefirstarch.feature.search.domain.usecase

import com.vladan.offlinefirstarch.core.common.Resource
import com.vladan.offlinefirstarch.feature.search.domain.model.Venue
import com.vladan.offlinefirstarch.feature.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchVenuesUseCase @Inject constructor(
    private val repository: SearchRepository,
) {
    operator fun invoke(query: String, latLng: String): Flow<Resource<List<Venue>>> {
        return repository.searchVenues(query, latLng)
    }
}
