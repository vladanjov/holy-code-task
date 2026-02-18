package com.vladan.holycodetask.feature.details.domain.repository

import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.feature.details.domain.model.VenueDetails
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {
    fun getVenueDetails(fsqId: String): Flow<Resource<VenueDetails>>
}
