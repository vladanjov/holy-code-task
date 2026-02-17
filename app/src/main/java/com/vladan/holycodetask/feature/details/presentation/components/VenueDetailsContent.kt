package com.vladan.holycodetask.feature.details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vladan.holycodetask.R
import com.vladan.holycodetask.feature.details.domain.model.VenueDetails

@Composable
fun VenueDetailsContent(
    venueDetails: VenueDetails,
    modifier: Modifier = Modifier,
) {
    val contentPadding = Modifier.padding(horizontal = 16.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        if (venueDetails.photos.isNotEmpty()) {
            PhotoCarousel(photos = venueDetails.photos)
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.ImageNotSupported,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (venueDetails.categoryName.isNotEmpty()) {
            InfoRow(
                icon = Icons.Default.Category,
                label = stringResource(R.string.category),
                value = venueDetails.categoryName,
                modifier = contentPadding,
            )
        }

        if (venueDetails.address.isNotEmpty()) {
            InfoRow(
                icon = Icons.Default.LocationOn,
                label = stringResource(R.string.address),
                value = venueDetails.address,
                modifier = contentPadding,
            )
        }

        if (venueDetails.hoursDisplay.isNotEmpty()) {
            InfoRow(
                icon = Icons.Default.AccessTime,
                label = if (venueDetails.openNow == true) stringResource(R.string.hours_open_now) else stringResource(R.string.hours),
                value = venueDetails.hoursDisplay,
                modifier = contentPadding,
            )
        }

        if (venueDetails.tel.isNotEmpty()) {
            InfoRow(
                icon = Icons.Default.Phone,
                label = stringResource(R.string.phone),
                value = venueDetails.tel,
                modifier = contentPadding,
            )
        }

        if (venueDetails.website.isNotEmpty()) {
            InfoRow(
                icon = Icons.Default.Public,
                label = stringResource(R.string.website),
                value = venueDetails.website,
                modifier = contentPadding,
            )
        }

        if (venueDetails.description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.about),
                style = MaterialTheme.typography.titleMedium,
                modifier = contentPadding,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = venueDetails.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = contentPadding,
            )
        }

        if (venueDetails.socialInstagram.isNotEmpty() || venueDetails.socialTwitter.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.social_media),
                style = MaterialTheme.typography.titleMedium,
                modifier = contentPadding,
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (venueDetails.socialInstagram.isNotEmpty()) {
                Text(
                    text = "Instagram: @${venueDetails.socialInstagram}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = contentPadding,
                )
            }
            if (venueDetails.socialTwitter.isNotEmpty()) {
                Text(
                    text = "Twitter: @${venueDetails.socialTwitter}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = contentPadding,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
