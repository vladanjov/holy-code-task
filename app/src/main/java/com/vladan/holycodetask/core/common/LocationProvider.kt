package com.vladan.holycodetask.core.common

interface LocationProvider {
    suspend fun getCurrentLocation(): Pair<Double, Double>?
}
