package com.vladan.holycodetask.core.common

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class FusedLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocationProvider {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    @Volatile
    private var cachedLocation: Pair<Double, Double>? = null

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Pair<Double, Double>? {
        cachedLocation?.let { return it }

        return suspendCancellableCoroutine { cont ->
            val cts = CancellationTokenSource()

            fusedClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
                .addOnSuccessListener { location ->
                    val result = location?.let {
                        Pair(it.latitude, it.longitude).also { pair -> cachedLocation = pair }
                    }
                    if (cont.isActive) cont.resume(result)
                }
                .addOnFailureListener {
                    if (cont.isActive) cont.resume(null)
                }
        }
    }
}
