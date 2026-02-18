package com.vladan.holycodetask.feature.details.domain.model

data class VenueDetails(
    val fsqId: String,
    val name: String,
    val categoryName: String,
    val categoryIconUrl: String,
    val address: String,
    val description: String,
    val tel: String,
    val website: String,
    val hoursDisplay: String,
    val openNow: Boolean?,
    val photos: List<String>,
    val latitude: Double?,
    val longitude: Double?,
    val socialInstagram: String,
    val socialTwitter: String,
)
