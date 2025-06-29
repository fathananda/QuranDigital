package com.fathi.qurandigital.features.quran.viewmodel

import com.fathi.qurandigital.data.model.SuratDetail
import com.fathi.qurandigital.data.model.Surat

data class QuranUiState(
    val suratList: List<Surat> = emptyList(),
    val selectedSurat: SuratDetail? = null,
    val isLoading: Boolean = false
)