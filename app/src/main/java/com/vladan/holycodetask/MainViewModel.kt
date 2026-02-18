package com.vladan.holycodetask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladan.holycodetask.core.network.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
) : ViewModel() {

    val isOffline: StateFlow<Boolean> = networkMonitor.isOnline
        .map { isOnline -> !isOnline }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = false
        )

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
