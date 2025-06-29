package com.fathi.qurandigital.features.quran.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fathi.qurandigital.features.quran.viewmodel.QuranViewModel

@Composable
fun QuranScreen(viewModel: QuranViewModel = viewModel()){
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.selectedSurat != null -> {
            SuratDetailScreen(
                surat = uiState.selectedSurat!!,
                onBackClick = { viewModel.clearSelectedSurat() }
            )
        }
        else -> {
            SuratListScreen(
                suratList = uiState.suratList,
                onSuratClick = { surat -> viewModel.selectSurat(surat) }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadSuratList()
    }

}