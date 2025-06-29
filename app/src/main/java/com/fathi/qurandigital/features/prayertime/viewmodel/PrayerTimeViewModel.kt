package com.fathi.qurandigital.features.prayertime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fathi.qurandigital.features.prayertime.viewmodel.PrayerTimeUiState
import com.fathi.qurandigital.core.location.LocationManager
import com.fathi.qurandigital.features.prayertime.data.PrayerTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PrayerTimeViewModel @Inject constructor(
    private val repository: PrayerTimeRepository,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrayerTimeUiState())
    val uiState: StateFlow<PrayerTimeUiState> = _uiState.asStateFlow()

    init {
        loadPrayerTimes()
        updateCurrentDate()
    }

    fun loadPrayerTimes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val location = locationManager.getCurrentLocation()
            if (location != null) {
                _uiState.value = _uiState.value.copy(
                    location = "${location.cityName}, ${location.countryName}"
                )

                repository.getPrayerTimes(location.latitude, location.longitude).fold(
                    onSuccess = { prayerTimes ->
                        _uiState.value = _uiState.value.copy(
                            prayerTimes = prayerTimes,
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
            } else {
                repository.getPrayerTimes().fold(
                    onSuccess = { prayerTimes ->
                        _uiState.value = _uiState.value.copy(
                            prayerTimes = prayerTimes,
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
            }
        }
    }

    fun updateLocation() {
        loadPrayerTimes()
    }

    private fun updateCurrentDate() {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        val currentDate = dateFormat.format(Date())
        _uiState.value = _uiState.value.copy(currentDate = currentDate)
    }
}