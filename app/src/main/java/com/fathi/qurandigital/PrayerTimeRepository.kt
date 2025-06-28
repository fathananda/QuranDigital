package com.fathi.qurandigital


import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerTimeRepository @Inject constructor(
    private val apiService: PrayerTimeApiService
) {
    suspend fun getPrayerTimes(
        latitude: Double = -6.2088,
        longitude: Double = 106.8456
    ): Result<List<PrayerTime>> {
        return try {
            val response: Response<PrayerTimeResponse> = apiService.getPrayerTimes(latitude, longitude)
            if (response.isSuccessful && response.body() != null) {
                val timings = response.body()!!.data.timings
                val prayerTimes = mapToPrayerTimeList(timings)
                Result.success(prayerTimes)
            } else {
                Result.failure(Exception("Failed to load prayer times"))
            }
        } catch (e: Exception) {
            Result.failure(e)
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
        ).filter { it.name != "Terbit" }
    }

    private fun formatTime(time: String): String {
        return time.split(" ")[0]
    }
}