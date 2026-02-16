package com.vladan.holycodetask.feature.search.domain.model

data class Venue(
    val fsqId: String,
    val name: String,
    val categoryName: String?,
    val address: String?,
    val distance: Int?,
)
