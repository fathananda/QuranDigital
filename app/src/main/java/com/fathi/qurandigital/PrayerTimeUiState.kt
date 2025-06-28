package com.fathi.qurandigital

data class PrayerTimeUiState(
    val prayerTimes: List<PrayerTime> = emptyList(),
    val location: String = "Jakarta, Indonesia",
    val currentDate: String = "Jumat, 27 Juni 2025",
    val nextPrayer: String = "",
    val isLoading: Boolean = false
)
