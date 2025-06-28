package com.fathi.qurandigital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fathi.qurandigital.MurottalTrack
import com.fathi.qurandigital.MurottalUiState
import com.fathi.qurandigital.Surat
import com.fathi.qurandigital.getSampleSuratList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MurottalViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MurottalUiState())
    val uiState: StateFlow<MurottalUiState> = _uiState.asStateFlow()

    fun loadSuratList() {
        viewModelScope.launch {
            val suratList = getSampleSuratList()
            _uiState.value = _uiState.value.copy(suratList = suratList)
        }
    }

    fun playSurat(surat: Surat) {
        val murottal = MurottalTrack(
            nomor = surat.nomor,
            namaLatin = surat.namaLatin,
            qari = "Mishary Rashid Al-Afasy"
        )
        _uiState.value = _uiState.value.copy(
            currentlyPlaying = murottal,
            isPlaying = true
        )
    }

    fun play() {
        _uiState.value = _uiState.value.copy(isPlaying = true)
    }

    fun pause() {
        _uiState.value = _uiState.value.copy(isPlaying = false)
    }

    fun nextTrack() {
        // Implement next track logic
    }

    fun previousTrack() {
        // Implement previous track logic
    }

    fun downloadSurat(surat: Surat) {
        viewModelScope.launch {
            val downloading = _uiState.value.downloadingSurats.toMutableSet()
            downloading.add(surat.nomor)
            _uiState.value = _uiState.value.copy(downloadingSurats = downloading)

            delay(3000)

            val downloaded = _uiState.value.downloadedSurats.toMutableSet()
            downloaded.add(surat.nomor)
            downloading.remove(surat.nomor)

            _uiState.value = _uiState.value.copy(
                downloadedSurats = downloaded,
                downloadingSurats = downloading
            )
        }
    }
}