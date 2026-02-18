package com.vladan.holycodetask.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vladan.holycodetask.core.database.dao.SearchResultDao
import com.vladan.holycodetask.core.database.dao.VenueDao
import com.vladan.holycodetask.core.database.entity.VenueEntity
import com.vladan.holycodetask.core.database.entity.VenueSearchResultEntity

@Database(
    entities = [VenueEntity::class, VenueSearchResultEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class HolyCodeTaskDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao
    abstract fun searchResultDao(): SearchResultDao
}
