package com.vladan.offlinefirstarch.feature.details.domain.repository

import com.vladan.offlinefirstarch.core.common.Resource
import com.vladan.offlinefirstarch.feature.details.domain.model.VenueDetails
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {
    fun getVenueDetails(fsqId: String): Flow<Resource<VenueDetails>>
}
