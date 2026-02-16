package com.vladan.holycodetask.feature.details.data.local

import com.vladan.holycodetask.core.database.dao.VenueDao
import com.vladan.holycodetask.core.database.entity.VenueEntity
import javax.inject.Inject

class DetailsLocalDataSource @Inject constructor(
    private val venueDao: VenueDao,
) {
    suspend fun getVenueById(fsqId: String): VenueEntity? {
        return venueDao.getVenueById(fsqId)
    }

    suspend fun cacheVenue(venue: VenueEntity) {
        venueDao.insertVenue(venue)
    }
}
