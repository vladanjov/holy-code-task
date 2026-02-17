package com.vladan.holycodetask.core.database.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "search_results",
    primaryKeys = ["query", "fsqId"],
    indices = [Index(value = ["fsqId"])],
)
data class VenueSearchResultEntity(
    val query: String,
    val fsqId: String,
    val position: Int,
    val cachedAt: Long = System.currentTimeMillis(),
)
