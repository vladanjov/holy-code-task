package com.vladan.holycodetask.feature.search.presentation

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.vladan.holycodetask.R
import com.vladan.holycodetask.core.common.UiEvent
import com.vladan.holycodetask.core.designsystem.EmptyScreen
import com.vladan.holycodetask.core.designsystem.LoadingScreen
import com.vladan.holycodetask.core.designsystem.OfflineBanner
import com.vladan.holycodetask.feature.search.presentation.components.SearchBar
import com.vladan.holycodetask.feature.search.presentation.components.VenueList

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SearchScreen(
    isOffline: Boolean,
    onVenueClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
    )

    LaunchedEffect(Unit) {
        if (!locationPermissions.allPermissionsGranted) {
            locationPermissions.launchMultiplePermissionRequest()
        }
    }

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
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            OfflineBanner(isOffline = isOffline)

            SearchBar(
                query = query,
                onQueryChange = viewModel::onQueryChange,
            )

            when {
                uiState.isLoading -> LoadingScreen()
                uiState.venues.isEmpty() && uiState.hasSearched -> {
                    EmptyScreen(message = stringResource(R.string.no_venues_found_try_a_different_search))
                }
                uiState.venues.isEmpty() && !uiState.hasSearched -> {
                    EmptyScreen(message = stringResource(R.string.search_for_venues_near_you))
                }
                else -> {
                    VenueList(
                        venues = uiState.venues,
                        onVenueClick = onVenueClick,
                    )
                }
            }
        }
    }
}
