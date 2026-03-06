package com.vladan.offlinefirstarch.feature.search.data.local

import androidx.room.withTransaction
import com.vladan.offlinefirstarch.core.common.runSuspendCatching
import com.vladan.offlinefirstarch.core.database.OfflineFirstArchDatabase
import com.vladan.offlinefirstarch.core.database.dao.SearchResultDao
import com.vladan.offlinefirstarch.core.database.dao.VenueDao
import com.vladan.offlinefirstarch.core.database.entity.VenueEntity
import com.vladan.offlinefirstarch.core.database.entity.VenueSearchResultEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchLocalDataSource @Inject constructor(
    private val database: OfflineFirstArchDatabase,
    private val venueDao: VenueDao,
    private val searchResultDao: SearchResultDao,
) {
    suspend fun getSafeSearchResults(query: String): List<VenueEntity>? {
        return runSuspendCatching { searchResultDao.getSearchResults(query) }
            .getOrNull()
            ?.ifEmpty { null }
    }

    fun observeSearchResults(query: String): Flow<List<VenueEntity>> {
        return searchResultDao.observeSearchResults(query)
    }

    suspend fun cacheSearchResults(query: String, venues: List<VenueEntity>) {
        database.withTransaction {
            venueDao.insertVenues(venues)
            searchResultDao.deleteSearchResults(query)
            val searchResults = venues.mapIndexed { index, venue ->
                VenueSearchResultEntity(
                    query = query,
                    fsqId = venue.fsqId,
                    position = index
                )
            }
            searchResultDao.insertSearchResults(searchResults)
        }
    }
}
