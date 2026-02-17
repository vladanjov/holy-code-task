package com.vladan.holycodetask.feature.search.presentation

import androidx.compose.runtime.Immutable
import com.vladan.holycodetask.feature.search.domain.model.Venue

@Immutable
data class SearchUiState(
    val venues: List<Venue> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
)
