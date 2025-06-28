package com.fathi.qurandigital

import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import retrofit2.Response


interface PrayerTimeApiService {
    @GET("v1/timings")
    suspend fun getPrayerTimes(
        @Query("latitude") latitude: Double = -6.2088,
        @Query("longitude") longitude: Double = 106.8456,
        @Query("method") method: Int = 20,
        @Query("date") date: String = getCurrentDate()
    ): Response<PrayerTimeResponse>
}

private fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(Date())
}