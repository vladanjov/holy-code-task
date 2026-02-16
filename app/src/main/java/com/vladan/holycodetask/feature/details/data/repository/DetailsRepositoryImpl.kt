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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val remoteDataSource: DetailsRemoteDataSource,
    private val localDataSource: DetailsLocalDataSource,
) : DetailsRepository {

    override fun getVenueDetails(fsqId: String): Flow<Resource<VenueDetails>> = channelFlow {
        send(Resource.Loading)

        // Network fetch in background
        launch {
            val result = runSuspendCatching {
                remoteDataSource.getPlaceDetails(fsqId)
            }
            result.fold(
                onSuccess = { dto ->
                    val cached = localDataSource.getVenueById(fsqId)
                    val entity = dto.toEntity().copy(distance = cached?.distance)
                    localDataSource.cacheVenue(entity)
                },
                onFailure = { throwable ->
                    send(Resource.Error(throwable.toAppError()))
                },
            )
        }

        // Room emits cached data immediately, then again when network writes new data
        localDataSource.observeVenueById(fsqId)
            .filterNotNull()
            .map { entity -> Resource.Success(entity.toVenueDetails()) }
            .collect { send(it) }
    }
}
