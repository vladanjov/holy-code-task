package com.vladan.offlinefirstarch.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vladan.offlinefirstarch.core.database.dao.SearchResultDao
import com.vladan.offlinefirstarch.core.database.dao.VenueDao
import com.vladan.offlinefirstarch.core.database.entity.VenueEntity
import com.vladan.offlinefirstarch.core.database.entity.VenueSearchResultEntity

@Database(
    entities = [VenueEntity::class, VenueSearchResultEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class OfflineFirstArchDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao
    abstract fun searchResultDao(): SearchResultDao
}
