package com.vladan.offlinefirstarch.core.common

interface LocationProvider {
    suspend fun getCurrentLocation(): Pair<Double, Double>?
}
