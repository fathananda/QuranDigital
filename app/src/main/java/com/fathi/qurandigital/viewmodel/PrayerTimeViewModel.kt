package com.fathi.qurandigital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fathi.qurandigital.PrayerTimeUiState
import com.fathi.qurandigital.getSamplePrayerTimes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PrayerTimeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PrayerTimeUiState())
    val uiState: StateFlow<PrayerTimeUiState> = _uiState.asStateFlow()

    fun loadPrayerTimes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val prayerTimes = getSamplePrayerTimes()
                _uiState.value = _uiState.value.copy(
                    prayerTimes = prayerTimes,
                    nextPrayer = "Maghrib",
                    isLoading = false
                )
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun updateLocation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(location = "Lokasi Baru")
        }
    }
}