package com.vladan.offlinefirstarch.feature.details.data.repository

import com.vladan.offlinefirstarch.core.common.Resource
import com.vladan.offlinefirstarch.core.common.runSuspendCatching
import com.vladan.offlinefirstarch.core.common.toAppError
import com.vladan.offlinefirstarch.feature.details.data.local.DetailsLocalDataSource
import com.vladan.offlinefirstarch.feature.details.data.mapper.toEntity
import com.vladan.offlinefirstarch.feature.details.data.mapper.toVenueDetails
import com.vladan.offlinefirstarch.feature.details.data.remote.DetailsRemoteDataSource
import com.vladan.offlinefirstarch.feature.details.domain.model.VenueDetails
import com.vladan.offlinefirstarch.feature.details.domain.repository.DetailsRepository
import kotlinx.coroutines.delay
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


        localDataSource.observeVenueById(fsqId)
            .filterNotNull()
            .map { entity -> Resource.Success(entity.toVenueDetails()) }
            .collect { send(it) }
    }
}
