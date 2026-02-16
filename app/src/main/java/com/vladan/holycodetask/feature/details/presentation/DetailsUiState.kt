package com.vladan.holycodetask.feature.details.presentation

import com.vladan.holycodetask.feature.details.domain.model.VenueDetails

data class DetailsUiState(
    val venueDetails: VenueDetails? = null,
    val isLoading: Boolean = true,
)
