package com.vladan.holycodetask.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Search : Route

    @Serializable
    data class Details(val venueId: String) : Route
}
