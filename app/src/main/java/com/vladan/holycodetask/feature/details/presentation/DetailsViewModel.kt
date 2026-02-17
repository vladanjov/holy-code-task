package com.vladan.holycodetask.feature.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.core.common.UiEvent
import com.vladan.holycodetask.core.common.toUiEvent
import com.vladan.holycodetask.feature.details.domain.usecase.GetVenueDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getVenueDetailsUseCase: GetVenueDetailsUseCase,
) : ViewModel() {

    private val venueId: String = checkNotNull(savedStateHandle["venueId"])

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val uiState: StateFlow<DetailsUiState> = getVenueDetailsUseCase(venueId)
        .scan(DetailsUiState()) { currentState, resource ->
            when (resource) {
                is Resource.Loading -> currentState.copy(isLoading = true)
                is Resource.Success -> DetailsUiState(venueDetails = resource.data)
                is Resource.Error -> {
                    resource.error.toUiEvent()?.let {
                        viewModelScope.launch { _uiEvent.send(it) }
                    }
                    currentState.copy(isLoading = false)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = DetailsUiState(),
        )

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
