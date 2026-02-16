package com.vladan.holycodetask.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vladan.holycodetask.core.database.entity.VenueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VenueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenues(venues: List<VenueEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenue(venue: VenueEntity)

    @Query("SELECT * FROM venues WHERE fsqId = :fsqId")
    suspend fun getVenueById(fsqId: String): VenueEntity?

    @Query("SELECT * FROM venues WHERE fsqId = :fsqId")
    fun observeVenueById(fsqId: String): Flow<VenueEntity?>
}
