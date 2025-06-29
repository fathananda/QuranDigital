package com.fathi.qurandigital.data.sample

import com.fathi.qurandigital.data.model.PrayerTime

fun getSamplePrayerTimes(): List<PrayerTime> {
    return listOf(
        PrayerTime("Subuh", "04:32"),
        PrayerTime("Syuruq", "05:54"),
        PrayerTime("Dzuhur", "12:05"),
        PrayerTime("Ashar", "15:18"),
        PrayerTime("Maghrib", "17:58"),
        PrayerTime("Isya", "19:10")
    )
}