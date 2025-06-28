package com.fathi.qurandigital.composable.alquran

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fathi.qurandigital.viewmodel.QuranViewModel

@Composable
fun QuranScreen(viewModel: QuranViewModel = viewModel()){
    val uiState by viewModel.uiState.collectAsState()

    Column (modifier = Modifier.fillMaxSize()){
        when{
            uiState.isLoading -> {
                Box (modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            uiState.selectedSurat != null -> {
                SuratDetailScreen(
                    surat = uiState.selectedSurat!!,
                    onBackClick = { viewModel.clearSelectedSurat() }
                )
            }

            else -> {
                SuratListScreen(
                    suratList = uiState.suratList,
                    onSuratClick = { viewModel.selectSurat(it) }
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadSuratList()
    }

}