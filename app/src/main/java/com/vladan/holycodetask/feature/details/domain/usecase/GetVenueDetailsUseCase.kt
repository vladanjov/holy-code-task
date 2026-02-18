package com.vladan.holycodetask.feature.details.domain.usecase

import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.feature.details.domain.model.VenueDetails
import com.vladan.holycodetask.feature.details.domain.repository.DetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVenueDetailsUseCase @Inject constructor(
    private val repository: DetailsRepository,
) {
    operator fun invoke(fsqId: String): Flow<Resource<VenueDetails>> {
        return repository.getVenueDetails(fsqId)
    }
}
