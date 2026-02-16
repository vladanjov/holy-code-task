package com.vladan.holycodetask.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venues")
data class VenueEntity(
    @PrimaryKey val fsqId: String,
    val name: String,
    val categoryName: String?,
    val categoryIconUrl: String?,
    val address: String?,
    val formattedAddress: String?,
    val locality: String?,
    val distance: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val description: String?,
    val tel: String?,
    val website: String?,
    val hoursDisplay: String?,
    val openNow: Boolean?,
    val rating: Double?,
    val price: Int?,
    val photosJson: String?,
    val socialFacebookId: String?,
    val socialInstagram: String?,
    val socialTwitter: String?,
    val updatedAt: Long = System.currentTimeMillis(),
)
