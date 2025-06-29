package com.fathi.qurandigital.features.murottal.viewmodel

import com.fathi.qurandigital.data.model.Surat

data class MurottalUiState(
    val suratList: List<Surat> = emptyList(),
    val currentlyPlaying: MurottalTrack? = null,
    val isPlaying: Boolean = false,
    val downloadedSurats: Set<Int> = emptySet(),
    val downloadingSurats: Set<Int> = emptySet(),
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val error: String? = null
)

data class MurottalTrack(
    val nomor: Int,
    val namaLatin: String,
    val qari: String
)
