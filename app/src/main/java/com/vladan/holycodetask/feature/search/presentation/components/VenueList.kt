package com.vladan.holycodetask.feature.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vladan.holycodetask.feature.search.domain.model.Venue

@Composable
fun VenueList(
    venues: List<Venue>,
    isOffline: Boolean,
    onVenueClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = venues,
            key = { it.fsqId }
        ) { venue ->
            VenueCard(
                venue = venue,
                showDistance = !isOffline,
                onClick = { onVenueClick(venue.fsqId) }
            )
        }
    }
}
