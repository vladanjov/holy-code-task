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
import androidx.compose.ui.unit.dp
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
            if (venueDetails.rating != null) {
                AssistChip(
                    onClick = {},
                    label = { Text("${venueDetails.rating}/10") },
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category
        if (venueDetails.categoryName != null) {
            InfoRow(
                icon = Icons.Default.Category,
                label = "Category",
                value = venueDetails.categoryName,
            )
        }

        // Address
        if (venueDetails.formattedAddress != null) {
            InfoRow(
                icon = Icons.Default.LocationOn,
                label = "Address",
                value = venueDetails.formattedAddress,
            )
        } else if (venueDetails.address != null) {
            InfoRow(
                icon = Icons.Default.LocationOn,
                label = "Address",
                value = venueDetails.address,
            )
        }

        // Hours
        if (venueDetails.hoursDisplay != null) {
            InfoRow(
                icon = Icons.Default.AccessTime,
                label = if (venueDetails.openNow == true) "Hours (Open now)" else "Hours",
                value = venueDetails.hoursDisplay,
            )
        }

        // Phone
        if (venueDetails.tel != null) {
            InfoRow(
                icon = Icons.Default.Phone,
                label = "Phone",
                value = venueDetails.tel,
            )
        }

        // Website
        if (venueDetails.website != null) {
            InfoRow(
                icon = Icons.Default.Public,
                label = "Website",
                value = venueDetails.website,
            )
        }

        // Price
        if (venueDetails.price != null) {
            InfoRow(
                icon = Icons.Default.AttachMoney,
                label = "Price",
                value = "$".repeat(venueDetails.price),
            )
        }

        // Rating
        if (venueDetails.rating != null) {
            InfoRow(
                icon = Icons.Default.Star,
                label = "Rating",
                value = "${venueDetails.rating}/10",
            )
        }

        // Description
        if (venueDetails.description != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = venueDetails.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        // Social media
        if (venueDetails.socialInstagram != null || venueDetails.socialTwitter != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Social Media",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (venueDetails.socialInstagram != null) {
                Text(
                    text = "Instagram: @${venueDetails.socialInstagram}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (venueDetails.socialTwitter != null) {
                Text(
                    text = "Twitter: @${venueDetails.socialTwitter}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
