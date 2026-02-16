package com.vladan.holycodetask.feature.details.data.repository

import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.core.common.runSuspendCatching
import com.vladan.holycodetask.core.common.toAppError
import com.vladan.holycodetask.feature.details.data.local.DetailsLocalDataSource
import com.vladan.holycodetask.feature.details.data.mapper.toEntity
import com.vladan.holycodetask.feature.details.data.mapper.toVenueDetails
import com.vladan.holycodetask.feature.details.data.remote.DetailsRemoteDataSource
import com.vladan.holycodetask.feature.details.domain.model.VenueDetails
import com.vladan.holycodetask.feature.details.domain.repository.DetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val remoteDataSource: DetailsRemoteDataSource,
    private val localDataSource: DetailsLocalDataSource,
) : DetailsRepository {

    override fun getVenueDetails(fsqId: String): Flow<Resource<VenueDetails>> = flow {
        emit(Resource.Loading)

        // Emit cached data first if available
        val cached = localDataSource.getVenueById(fsqId)
        if (cached != null) {
            emit(Resource.Success(cached.toVenueDetails()))
        }

        // Try to fetch fresh data from network
        val result = runSuspendCatching {
            remoteDataSource.getPlaceDetails(fsqId)
        }

        result.fold(
            onSuccess = { dto ->
                val entity = dto.toEntity().copy(distance = cached?.distance)
                localDataSource.cacheVenue(entity)
                emit(Resource.Success(entity.toVenueDetails()))
            },
            onFailure = { throwable ->
                if (cached == null) {
                    emit(Resource.Error(throwable.toAppError()))
                }
            },
        )
    }
}
