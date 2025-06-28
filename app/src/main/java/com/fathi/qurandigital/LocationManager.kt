package com.fathi.qurandigital

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

data class LocationInfo(
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val countryName: String
)

@Singleton
class LocationManager @Inject constructor(
    private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val geocoder = Geocoder(context, Locale.getDefault())

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationInfo? {
        return suspendCancellableCoroutine { continuation ->
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        try {
                            val addresses = geocoder.getFromLocation(
                                location.latitude,
                                location.longitude,
                                1
                            )

                            val cityName = addresses?.firstOrNull()?.locality ?: "Unknown City"
                            val countryName = addresses?.firstOrNull()?.countryName ?: "Unknown Country"

                            val locationInfo = LocationInfo(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                cityName = cityName,
                                countryName = countryName
                            )

                            continuation.resume(locationInfo)
                        } catch (e: Exception) {
                            // If geocoding fails, still return location with coordinates
                            val locationInfo = LocationInfo(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                cityName = "Unknown City",
                                countryName = "Unknown Country"
                            )
                            continuation.resume(locationInfo)
                        }
                    } else {
                        // Return default Jakarta location if location is null
                        val defaultLocation = LocationInfo(
                            latitude = -6.2088,
                            longitude = 106.8456,
                            cityName = "Jakarta",
                            countryName = "Indonesia"
                        )
                        continuation.resume(defaultLocation)
                    }
                }.addOnFailureListener {
                    // Return default Jakarta location on failure
                    val defaultLocation = LocationInfo(
                        latitude = -6.2088,
                        longitude = 106.8456,
                        cityName = "Jakarta",
                        countryName = "Indonesia"
                    )
                    continuation.resume(defaultLocation)
                }
            } catch (e: Exception) {
                val defaultLocation = LocationInfo(
                    latitude = -6.2088,
                    longitude = 106.8456,
                    cityName = "Jakarta",
                    countryName = "Indonesia"
                )
                continuation.resume(defaultLocation)
            }
        }
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}