package com.vladan.offlinefirstarch.feature.search.presentation

import androidx.compose.runtime.Immutable
import com.vladan.offlinefirstarch.feature.search.domain.model.Venue

@Immutable
data class SearchUiState(
    val venues: List<Venue> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
)
