package com.vladan.holycodetask.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vladan.holycodetask.core.database.entity.VenueEntity
import com.vladan.holycodetask.core.database.entity.VenueSearchResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResults(results: List<VenueSearchResultEntity>)

    @Query("DELETE FROM search_results WHERE `query` = :query")
    suspend fun deleteSearchResults(query: String)

    @Transaction
    @Query(
        """
        SELECT v.* FROM venues v
        INNER JOIN search_results sr ON v.fsqId = sr.fsqId
        WHERE sr.`query` = :query
        ORDER BY sr.position ASC
        """
    )
    suspend fun getSearchResults(query: String): List<VenueEntity>

    @Transaction
    @Query(
        """
        SELECT v.* FROM venues v
        INNER JOIN search_results sr ON v.fsqId = sr.fsqId
        WHERE sr.`query` = :query
        ORDER BY sr.position ASC
        """
    )
    fun observeSearchResults(query: String): Flow<List<VenueEntity>>
}
