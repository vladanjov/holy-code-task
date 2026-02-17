package com.vladan.holycodetask.feature.details.presentation

import androidx.compose.runtime.Immutable
import com.vladan.holycodetask.feature.details.domain.model.VenueDetails

@Immutable
data class DetailsUiState(
    val venueDetails: VenueDetails? = null,
    val isLoading: Boolean = true,
)
