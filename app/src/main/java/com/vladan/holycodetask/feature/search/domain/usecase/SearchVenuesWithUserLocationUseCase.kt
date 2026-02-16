package com.vladan.holycodetask.feature.search.domain.usecase

import com.vladan.holycodetask.core.common.GetUserLocationUseCase
import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.feature.search.domain.model.Venue
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchVenuesWithUserLocationUseCase @Inject constructor(
    private val searchVenuesUseCase: SearchVenuesUseCase,
    private val getUserLocationUseCase: GetUserLocationUseCase,
) {
    suspend operator fun invoke(query: String): Flow<Resource<List<Venue>>> {
        val latLng = getUserLocationUseCase()
        return searchVenuesUseCase(query, latLng)
    }
}
