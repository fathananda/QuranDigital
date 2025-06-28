package com.fathi.qurandigital


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerTimeRepository @Inject constructor(
    private val apiService: PrayerTimeApiService
) {
    suspend fun getPrayerTimes(
        latitude: Double = -6.2088, // Jakarta default
        longitude: Double = 106.8456 // Jakarta default
    ): kotlin.Result<List<PrayerTime>> {
        return try {
            val response = apiService.getPrayerTimes(latitude, longitude)
            if (response.isSuccessful && response.body() != null) {
                val timings = response.body()!!.data.timings
                val prayerTimes = mapToPrayerTimeList(timings)
                kotlin.Result.success(prayerTimes)
            } else {
                kotlin.Result.failure(Exception("Failed to load prayer times"))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }

    private fun mapToPrayerTimeList(timings: PrayerTimings): List<PrayerTime> {
        return listOf(
            PrayerTime("Subuh", formatTime(timings.Fajr)),
            PrayerTime("Terbit", formatTime(timings.Sunrise)),
            PrayerTime("Dzuhur", formatTime(timings.Dhuhr)),
            PrayerTime("Ashar", formatTime(timings.Asr)),
            PrayerTime("Maghrib", formatTime(timings.Maghrib)),
            PrayerTime("Isya", formatTime(timings.Isha))
        ).filter { it.name != "Terbit" } // Remove sunrise from prayer times
    }

    private fun formatTime(time: String): String {
        // Remove timezone information if present (e.g., "05:30 (WIB)" -> "05:30")
        return time.split(" ")[0]
    }
}