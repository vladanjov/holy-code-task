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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getVenueDetailsUseCase: GetVenueDetailsUseCase,
) : ViewModel() {

    private val venueId: String = checkNotNull(savedStateHandle["venueId"])

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadDetails()
    }

    fun loadDetails() {
        viewModelScope.launch {
            getVenueDetailsUseCase(venueId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, isError = false) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                venueDetails = resource.data,
                                isLoading = false,
                                isError = false,
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, isError = true) }
                        resource.error.toUiEvent()?.let {
                            _uiEvent.send(it)
                        }
                    }
                }
            }
        }
    }
}
