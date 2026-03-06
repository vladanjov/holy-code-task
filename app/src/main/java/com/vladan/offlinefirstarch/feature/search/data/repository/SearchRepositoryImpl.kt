package com.vladan.offlinefirstarch.feature.search.data.repository

import com.vladan.offlinefirstarch.core.common.Resource
import com.vladan.offlinefirstarch.core.common.runSuspendCatching
import com.vladan.offlinefirstarch.core.common.toAppError
import com.vladan.offlinefirstarch.feature.search.data.local.SearchLocalDataSource
import com.vladan.offlinefirstarch.feature.search.data.mapper.toEntity
import com.vladan.offlinefirstarch.feature.search.data.mapper.toVenue
import com.vladan.offlinefirstarch.feature.search.data.remote.SearchRemoteDataSource
import com.vladan.offlinefirstarch.feature.search.domain.model.Venue
import com.vladan.offlinefirstarch.feature.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchRemoteDataSource,
    private val localDataSource: SearchLocalDataSource,
) : SearchRepository {

    override fun searchVenues(query: String, latLng: String): Flow<Resource<List<Venue>>> = channelFlow {
        send(Resource.Loading)

        launch {
            val result = runSuspendCatching {
                remoteDataSource.searchPlaces(query, latLng)
            }
            result.fold(
                onSuccess = { dtos ->
                    localDataSource.cacheSearchResults(query, dtos.map { it.toEntity() })
                },
                onFailure = { throwable ->
                    send(Resource.Error(throwable.toAppError()))
                },
            )
        }

        localDataSource.observeSearchResults(query)
            .filter { it.isNotEmpty() }
            .map { entities -> Resource.Success(entities.map { it.toVenue() }) }
            .collect { send(it) }
    }
}
