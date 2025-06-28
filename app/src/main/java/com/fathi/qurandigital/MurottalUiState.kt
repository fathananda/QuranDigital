package com.fathi.qurandigital

data class MurottalUiState(
    val suratList: List<Surat> = emptyList(),
    val currentlyPlaying: MurottalTrack? = null,
    val isPlaying: Boolean = false,
    val downloadedSurats: Set<Int> = emptySet(),
    val downloadingSurats: Set<Int> = emptySet()
)

data class MurottalTrack(
    val nomor: Int,
    val namaLatin: String,
    val qari: String
)
