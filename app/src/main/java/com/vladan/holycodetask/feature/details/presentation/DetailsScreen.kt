package com.vladan.holycodetask.feature.details.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vladan.holycodetask.R
import com.vladan.holycodetask.core.common.UiEvent
import com.vladan.holycodetask.core.designsystem.EmptyScreen
import com.vladan.holycodetask.core.designsystem.LoadingScreen
import com.vladan.holycodetask.core.designsystem.OfflineBanner
import com.vladan.holycodetask.feature.details.presentation.components.VenueDetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    isOffline: Boolean,
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.venueDetails?.name ?: stringResource(R.string.details),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            OfflineBanner(isOffline = isOffline)

            when {
                uiState.isLoading && uiState.venueDetails == null -> LoadingScreen()
                uiState.isError && uiState.venueDetails == null -> {
                    EmptyScreen(message = stringResource(R.string.failed_to_load_venue_details))
                }
                uiState.venueDetails != null -> {
                    VenueDetailsContent(venueDetails = uiState.venueDetails!!)
                }
            }
        }
    }
}
