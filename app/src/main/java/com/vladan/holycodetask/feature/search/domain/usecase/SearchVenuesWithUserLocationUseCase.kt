package com.vladan.holycodetask.feature.search.domain.usecase

import com.vladan.holycodetask.core.common.GetUserLocationUseCase
import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.feature.search.domain.model.Venue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchVenuesWithUserLocationUseCase @Inject constructor(
    private val searchVenuesUseCase: SearchVenuesUseCase,
    private val getUserLocationUseCase: GetUserLocationUseCase,
) {
    operator fun invoke(query: String): Flow<Resource<List<Venue>>> = flow {
        val latLng = getUserLocationUseCase()
        emitAll(searchVenuesUseCase(query, latLng))
    }
}
