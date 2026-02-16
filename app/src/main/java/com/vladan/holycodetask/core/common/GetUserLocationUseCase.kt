package com.vladan.holycodetask.core.common

import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider,
) {
    suspend operator fun invoke(): String {
        val (lat, lng) = locationProvider.getCurrentLocation()
            ?: Pair(DEFAULT_LAT, DEFAULT_LNG)
        return "$lat,$lng"
    }

    companion object {
        private const val DEFAULT_LAT = 44.8176
        private const val DEFAULT_LNG = 20.4633
    }
}
