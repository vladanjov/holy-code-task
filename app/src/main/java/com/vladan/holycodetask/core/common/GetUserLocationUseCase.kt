package com.vladan.holycodetask.core.common

import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider,
) {
    suspend operator fun invoke(): String {
        val (lat, lng) = runSuspendCatching { locationProvider.getCurrentLocation() }
            .getOrNull()
            ?: Pair(AppConfig.DEFAULT_LAT, AppConfig.DEFAULT_LNG)
        return "$lat,$lng"
    }
}
