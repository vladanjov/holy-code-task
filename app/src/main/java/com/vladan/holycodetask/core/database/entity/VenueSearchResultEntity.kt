package com.vladan.holycodetask.core.database.entity

import androidx.room.Entity

@Entity(tableName = "search_results", primaryKeys = ["query", "fsqId"])
data class VenueSearchResultEntity(
    val query: String,
    val fsqId: String,
    val position: Int,
    val cachedAt: Long = System.currentTimeMillis(),
)
