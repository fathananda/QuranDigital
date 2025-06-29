package com.fathi.qurandigital.features.quran.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fathi.qurandigital.features.quran.data.QuranRepository
import com.fathi.qurandigital.features.quran.viewmodel.QuranUiState
import com.fathi.qurandigital.data.model.Surat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(
    private val repository: QuranRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuranUiState())
    val uiState: StateFlow<QuranUiState> = _uiState.asStateFlow()

    init {
        loadSuratList()
    }
    fun loadSuratList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getSuratList().fold(
                    onSuccess = { suratList ->
                        _uiState.value = _uiState.value.copy(
                            suratList = suratList,
                            isLoading = false
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false)
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false)
            }
        }
    }

    fun selectSurat(surat: Surat) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getSuratDetail(surat.nomor).fold(
                    onSuccess = { suratDetail ->
                        _uiState.value = _uiState.value.copy(
                            selectedSurat = suratDetail,
                            isLoading = false
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,)
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false)
            }
        }
    }

    fun clearSelectedSurat() {
        _uiState.value = _uiState.value.copy(selectedSurat = null)
    }

}