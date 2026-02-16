package com.vladan.holycodetask.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.vladan.holycodetask.feature.details.presentation.DetailsScreen
import com.vladan.holycodetask.feature.search.presentation.SearchScreen

@Composable
fun HolyCodeTaskNavHost(
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
            val route = backStackEntry.toRoute<Route.Details>()
            DetailsScreen(
                venueId = route.venueId,
                isOffline = isOffline,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
