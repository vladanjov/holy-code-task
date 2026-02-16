package com.vladan.holycodetask.feature.details.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vladan.holycodetask.R
import com.vladan.holycodetask.feature.details.domain.model.VenueDetails

@Composable
fun VenueDetailsContent(
    venueDetails: VenueDetails,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // Photo carousel
        if (venueDetails.photos.isNotEmpty()) {
            PhotoCarousel(photos = venueDetails.photos)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Name and rating
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = venueDetails.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f),
            )
            venueDetails.rating?.let { rating ->
                AssistChip(
                    onClick = {},
                    label = { Text("$rating/10") },
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category
        venueDetails.categoryName?.let { categoryName ->
            InfoRow(
                icon = Icons.Default.Category,
                label = stringResource(R.string.category),
                value = categoryName,
            )
        }

        // Address
        (venueDetails.formattedAddress ?: venueDetails.address)?.let { address ->
            InfoRow(
                icon = Icons.Default.LocationOn,
                label = stringResource(R.string.address),
                value = address,
            )
        }

        // Hours
        venueDetails.hoursDisplay?.let { hoursDisplay ->
            InfoRow(
                icon = Icons.Default.AccessTime,
                label = if (venueDetails.openNow == true) stringResource(R.string.hours_open_now) else stringResource(R.string.hours),
                value = hoursDisplay,
            )
        }

        // Phone
        venueDetails.tel?.let { tel ->
            InfoRow(
                icon = Icons.Default.Phone,
                label = stringResource(R.string.phone),
                value = tel,
            )
        }

        // Website
        venueDetails.website?.let { website ->
            InfoRow(
                icon = Icons.Default.Public,
                label = stringResource(R.string.website),
                value = website,
            )
        }

        // Price
        venueDetails.price?.let { price ->
            InfoRow(
                icon = Icons.Default.AttachMoney,
                label = stringResource(R.string.price),
                value = "$".repeat(price),
            )
        }

        // Rating
        venueDetails.rating?.let { rating ->
            InfoRow(
                icon = Icons.Default.Star,
                label = stringResource(R.string.rating),
                value = "$rating/10",
            )
        }

        // Description
        venueDetails.description?.let { description ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.about),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        // Social media
        if (venueDetails.socialInstagram != null || venueDetails.socialTwitter != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.social_media),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            venueDetails.socialInstagram?.let { instagram ->
                Text(
                    text = "Instagram: @$instagram",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            venueDetails.socialTwitter?.let { twitter ->
                Text(
                    text = "Twitter: @$twitter",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
