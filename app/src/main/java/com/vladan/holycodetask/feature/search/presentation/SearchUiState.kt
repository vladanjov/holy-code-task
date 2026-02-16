package com.vladan.holycodetask.feature.search.presentation

import com.vladan.holycodetask.feature.search.domain.model.Venue

data class SearchUiState(
    val venues: List<Venue> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
)
