package com.fathi.qurandigital.data.response

data class PrayerTimeResponse(
    val code: Int,
    val status: String,
    val data: PrayerTimeData
)

data class PrayerTimeData(
    val timings: PrayerTimings,
    val date: DateInfo
)

data class PrayerTimings(
    val Fajr: String,
    val Sunrise: String,
    val Dhuhr: String,
    val Asr: String,
    val Sunset: String,
    val Maghrib: String,
    val Isha: String
)

data class DateInfo(
    val readable: String,
    val timestamp: String,
    val hijri: HijriDate,
    val gregorian: GregorianDate
)

data class HijriDate(
    val date: String,
    val format: String,
    val day: String,
    val weekday: WeekdayInfo,
    val month: MonthInfo,
    val year: String
)

data class GregorianDate(
    val date: String,
    val format: String,
    val day: String,
    val weekday: WeekdayInfo,
    val month: MonthInfo,
    val year: String
)

data class WeekdayInfo(
    val en: String,
    val ar: String
)

data class MonthInfo(
    val number: Int,
    val en: String,
    val ar: String
)
