package com.fathi.qurandigital.features.murottal.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fathi.qurandigital.features.quran.data.QuranRepository
import com.fathi.qurandigital.core.audio.AudioManager
import com.fathi.qurandigital.data.model.Surat
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MurottalViewModel @Inject constructor(
    private val repository: QuranRepository,
    private val audioManager: AudioManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(MurottalUiState())
    val uiState: StateFlow<MurottalUiState> = _uiState.asStateFlow()

    init {
        loadSuratList()
        observeAudioState()
    }

    fun loadSuratList() {
        viewModelScope.launch {
            repository.getSuratList().fold(
                onSuccess = { suratList ->
                    _uiState.value = _uiState.value.copy(suratList = suratList)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
            )
        }
    }

    private fun observeAudioState() {
        viewModelScope.launch {
            audioManager.isPlaying.collect { isPlaying ->
                _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
            }
        }

        viewModelScope.launch {
            audioManager.currentPosition.collect { position ->
                _uiState.value = _uiState.value.copy(currentPosition = position)
            }
        }

        viewModelScope.launch {
            audioManager.duration.collect { duration ->
                _uiState.value = _uiState.value.copy(duration = duration)
            }
        }
    }

    fun playSurat(surat: Surat, qari: String = "Mishary Rashid Al-Afasy") {
        viewModelScope.launch {
            try {
                repository.getSuratDetail(surat.nomor).fold(
                    onSuccess = { suratDetail ->
                        val audioUrl = generateAudioUrl(surat.nomor, qari)
                        val murottal = MurottalTrack(
                            nomor = surat.nomor,
                            namaLatin = surat.namaLatin,
                            qari = qari
                        )

                        _uiState.value = _uiState.value.copy(currentlyPlaying = murottal)
                        audioManager.playAudio(audioUrl)
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(error = error.message)
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun play() {
        audioManager.resume()
    }

    fun pause() {
        audioManager.pause()
    }

    fun stop() {
        audioManager.stop()
        _uiState.value = _uiState.value.copy(
            currentlyPlaying = null,
            isPlaying = false,
            currentPosition = 0L,
            duration = 0L
        )
    }

    fun seekTo(position: Long) {
        audioManager.seekTo(position)
    }

    fun nextTrack() {
        val currentSurat = _uiState.value.currentlyPlaying
        if (currentSurat != null) {
            val suratList = _uiState.value.suratList
            val currentIndex = suratList.indexOfFirst { it.nomor == currentSurat.nomor }
            if (currentIndex != -1 && currentIndex < suratList.size - 1) {
                playSurat(suratList[currentIndex + 1], currentSurat.qari)
            }
        }
    }

    fun previousTrack() {
        val currentSurat = _uiState.value.currentlyPlaying
        if (currentSurat != null) {
            val suratList = _uiState.value.suratList
            val currentIndex = suratList.indexOfFirst { it.nomor == currentSurat.nomor }
            if (currentIndex > 0) {
                playSurat(suratList[currentIndex - 1], currentSurat.qari)
            }
        }
    }

    fun downloadSurat(surat: Surat, qari: String = "Mishary Rashid Al-Afasy") {
        viewModelScope.launch {
            try {
                val downloading = _uiState.value.downloadingSurats.toMutableSet()
                downloading.add(surat.nomor)
                _uiState.value = _uiState.value.copy(downloadingSurats = downloading)

                val audioUrl = generateAudioUrl(surat.nomor, qari)
                val success = audioManager.downloadAudio(audioUrl, surat.nomor.toString())

                if (success) {
                    val downloaded = _uiState.value.downloadedSurats.toMutableSet()
                    downloaded.add(surat.nomor)
                    downloading.remove(surat.nomor)

                    _uiState.value = _uiState.value.copy(
                        downloadedSurats = downloaded,
                        downloadingSurats = downloading
                    )
                } else {
                    downloading.remove(surat.nomor)
                    _uiState.value = _uiState.value.copy(
                        downloadingSurats = downloading,
                        error = "Failed to download ${surat.namaLatin}"
                    )
                }
            } catch (e: Exception) {
                val downloading = _uiState.value.downloadingSurats.toMutableSet()
                downloading.remove(surat.nomor)
                _uiState.value = _uiState.value.copy(
                    downloadingSurats = downloading,
                    error = e.message
                )
            }
        }
    }

    private fun generateAudioUrl(suratNumber: Int, qari: String): String {
        return when (qari) {
            "Mishary Rashid Al-Afasy" -> {
                val formattedNumber = String.format("%03d", suratNumber)
                "https://server8.mp3quran.net/afs/$formattedNumber.mp3"
            }
            "Abdul Rahman Al-Sudais" -> {
                val formattedNumber = String.format("%03d", suratNumber)
                "https://server11.mp3quran.net/sds/$formattedNumber.mp3"
            }
            "Maher Al-Muaiqly" -> {
                val formattedNumber = String.format("%03d", suratNumber)
                "https://server12.mp3quran.net/maher/$formattedNumber.mp3"
            }
            else -> {
                val formattedNumber = String.format("%03d", suratNumber)
                "https://server8.mp3quran.net/afs/$formattedNumber.mp3"
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.release()
    }
}