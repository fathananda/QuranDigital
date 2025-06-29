package com.fathi.qurandigital.features.prayertime.viewmodel

import com.fathi.qurandigital.data.model.PrayerTime

data class PrayerTimeUiState(
    val prayerTimes: List<PrayerTime> = emptyList(),
    val location: String = "Jakarta, Indonesia",
    val currentDate: String = "",
    val currentTime: String = "",
    val nextPrayer: String = "",
    val nextPrayerTime: String = "",
    val timeUntilNext: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)