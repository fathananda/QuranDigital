package com.fathi.qurandigital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fathi.qurandigital.QuranUiState
import com.fathi.qurandigital.Surat
import com.fathi.qurandigital.getSampleSuratDetail
import com.fathi.qurandigital.getSampleSuratList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuranViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuranUiState())
    val uiState: StateFlow<QuranUiState> = _uiState.asStateFlow()

    fun loadSuratList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val suratList = getSampleSuratList()
                _uiState.value = _uiState.value.copy(
                    suratList = suratList,
                    isLoading = false
                )
            } catch (_: Exception) { // DIPERBAIKI: _ untuk unused parameter
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun selectSurat(surat: Surat) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val suratDetail = getSampleSuratDetail(surat)
                _uiState.value = _uiState.value.copy(
                    selectedSurat = suratDetail,
                    isLoading = false
                )
            } catch (_: Exception) { // DIPERBAIKI: _ untuk unused parameter
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun clearSelectedSurat() {
        _uiState.value = _uiState.value.copy(selectedSurat = null)
    }
}