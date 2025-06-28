package com.fathi.qurandigital

data class QuranUiState(
    val suratList: List<Surat> = emptyList(),
    val selectedSurat: SuratDetail? = null,
    val isLoading: Boolean = false
)
