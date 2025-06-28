package com.fathi.qurandigital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fathi.qurandigital.PrayerTime
import com.fathi.qurandigital.PrayerTimeRepository
import com.fathi.qurandigital.PrayerTimeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PrayerTimeViewModel @Inject constructor(
    private val repository: PrayerTimeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PrayerTimeUiState())
    val uiState: StateFlow<PrayerTimeUiState> = _uiState.asStateFlow()

    init {
        loadPrayerTimes()
        startTimeUpdater()
    }

    fun loadPrayerTimes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getPrayerTimes().fold(
                    onSuccess = { prayerTimes ->
                        val nextPrayerInfo = calculateNextPrayer(prayerTimes)
                        _uiState.value = _uiState.value.copy(
                            prayerTimes = prayerTimes,
                            nextPrayer = nextPrayerInfo.first,
                            nextPrayerTime = nextPrayerInfo.second,
                            currentDate = getCurrentDateString(),
                            isLoading = false
                        )
                        calculateTimeUntilNext()
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun startTimeUpdater() {
        viewModelScope.launch {
            while (true) {
                _uiState.value = _uiState.value.copy(
                    currentTime = getCurrentTimeString()
                )
                calculateTimeUntilNext()
                delay(1000) // Update every second
            }
        }
    }

    private fun calculateNextPrayer(prayerTimes: List<PrayerTime>): Pair<String, String> {
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(Calendar.MINUTE)
        val currentTimeInMinutes = currentHour * 60 + currentMinute

        for (prayer in prayerTimes) {
            val prayerTime = prayer.time.split(":")
            val prayerHour = prayerTime[0].toInt()
            val prayerMinute = prayerTime[1].toInt()
            val prayerTimeInMinutes = prayerHour * 60 + prayerMinute

            if (prayerTimeInMinutes > currentTimeInMinutes) {
                return Pair(prayer.name, prayer.time)
            }
        }

        // If no prayer found for today, return the first prayer of tomorrow
        return if (prayerTimes.isNotEmpty()) {
            Pair(prayerTimes[0].name, prayerTimes[0].time)
        } else {
            Pair("", "")
        }
    }

    private fun calculateTimeUntilNext() {
        val currentState = _uiState.value
        if (currentState.nextPrayerTime.isNotEmpty()) {
            val currentTime = Calendar.getInstance()
            val nextPrayerTime = Calendar.getInstance()

            val timeParts = currentState.nextPrayerTime.split(":")
            nextPrayerTime.set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
            nextPrayerTime.set(Calendar.MINUTE, timeParts[1].toInt())
            nextPrayerTime.set(Calendar.SECOND, 0)

            // If next prayer is tomorrow
            if (nextPrayerTime.before(currentTime)) {
                nextPrayerTime.add(Calendar.DAY_OF_MONTH, 1)
            }

            val diffInMillis = nextPrayerTime.timeInMillis - currentTime.timeInMillis
            val hours = diffInMillis / (1000 * 60 * 60)
            val minutes = (diffInMillis % (1000 * 60 * 60)) / (1000 * 60)
            val seconds = (diffInMillis % (1000 * 60)) / 1000

            val timeUntilNext = when {
                hours > 0 -> "${hours}j ${minutes}m ${seconds}d"
                minutes > 0 -> "${minutes}m ${seconds}d"
                else -> "${seconds}d"
            }

            _uiState.value = _uiState.value.copy(timeUntilNext = timeUntilNext)
        }
    }

    private fun getCurrentDateString(): String {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return dateFormat.format(Date())
    }

    private fun getCurrentTimeString(): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(Date())
    }

    fun updateLocation(latitude: Double, longitude: Double, locationName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                location = locationName,
                isLoading = true
            )
            try {
                repository.getPrayerTimes(latitude, longitude).fold(
                    onSuccess = { prayerTimes ->
                        val nextPrayerInfo = calculateNextPrayer(prayerTimes)
                        _uiState.value = _uiState.value.copy(
                            prayerTimes = prayerTimes,
                            nextPrayer = nextPrayerInfo.first,
                            nextPrayerTime = nextPrayerInfo.second,
                            isLoading = false
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}