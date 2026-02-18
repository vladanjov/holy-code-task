package com.vladan.holycodetask.feature.search.data.repository

import com.vladan.holycodetask.core.common.AppError
import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.core.common.runSuspendCatching
import com.vladan.holycodetask.core.common.toAppError
import com.vladan.holycodetask.feature.search.data.local.SearchLocalDataSource
import com.vladan.holycodetask.feature.search.data.mapper.toEntity
import com.vladan.holycodetask.feature.search.data.mapper.toVenue
import com.vladan.holycodetask.feature.search.data.remote.SearchRemoteDataSource
import com.vladan.holycodetask.feature.search.domain.model.Venue
import com.vladan.holycodetask.feature.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchRemoteDataSource,
    private val localDataSource: SearchLocalDataSource,
) : SearchRepository {

    override fun searchVenues(query: String, latLng: String): Flow<Resource<List<Venue>>> = flow {
        emit(Resource.Loading)

        val result = runSuspendCatching {
            remoteDataSource.searchPlaces(query, latLng)
        }

        result.fold(
            onSuccess = { dtos ->
                localDataSource.cacheSearchResults(query, dtos.map { it.toEntity() })
            },
            onFailure = { throwable ->
                val appError = throwable.toAppError()
                val cached = if (appError is AppError.NetworkError) {
                    localDataSource.getSafeSearchResults(query)?.map { it.toVenue() }
                } else {
                    null
                }
                emit(Resource.Error(appError, cached))
                return@flow
            },
        )

        emitAll(
            localDataSource.observeSearchResults(query)
                .map { entities -> Resource.Success(entities.map { it.toVenue() }) }
        )
    }
}
