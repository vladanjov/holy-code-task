package com.vladan.offlinefirstarch.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vladan.offlinefirstarch.feature.details.presentation.DetailsScreen
import com.vladan.offlinefirstarch.feature.search.presentation.SearchScreen

@Composable
fun OfflineFirstArchNavHost(
    navController: NavHostController,
    isOffline: Boolean,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Route.Search,
        modifier = modifier,
    ) {
        composable<Route.Search> {
            SearchScreen(
                isOffline = isOffline,
                onVenueClick = { venueId ->
                    navController.navigate(Route.Details(venueId))
                },
            )
        }
        composable<Route.Details> { backStackEntry ->
            DetailsScreen(
                isOffline = isOffline,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
