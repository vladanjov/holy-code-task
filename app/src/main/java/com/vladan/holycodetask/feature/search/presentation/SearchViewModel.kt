package com.vladan.holycodetask.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.core.common.UiEvent
import com.vladan.holycodetask.core.common.toUiEvent
import com.vladan.holycodetask.feature.search.domain.usecase.SearchVenuesWithUserLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchVenuesWithUserLocation: SearchVenuesWithUserLocationUseCase,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    val uiState: StateFlow<SearchUiState> = _query
        .debounce(400)
        .flatMapLatest { query ->
            if (query.length < 2) {
                flowOf(SearchUiState())
            } else {
                searchVenuesWithUserLocation(query).map { resource ->
                    when (resource) {
                        is Resource.Loading -> SearchUiState(isLoading = true)
                        is Resource.Success -> SearchUiState(
                            venues = resource.data,
                            hasSearched = true
                        )

                        is Resource.Error -> {
                            resource.error.toUiEvent()?.let {
                                viewModelScope.launch {
                                    _uiEvent.emit(it)
                                }
                            }
                            SearchUiState(
                                venues = resource.data ?: emptyList(),
                                hasSearched = true
                            )
                        }
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchUiState(),
        )

    fun onQueryChange(query: String) {
        _query.update { query }
    }
}
