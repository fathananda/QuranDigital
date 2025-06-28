package com.fathi.qurandigital.composable.alquran

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fathi.qurandigital.Surat

@Composable
fun SuratListScreen(
    suratList : List<Surat>,
    onSuratClick : (Surat) -> Unit
){
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(suratList){ surat ->
            Card (
                modifier = Modifier.fillMaxWidth()
                    .clickable{ onSuratClick(surat)},
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ){
                Row (
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween

                ){
                    Column (modifier = Modifier.weight(1f)){
                        Text(
                            text = "${surat.nomor}. ${surat.namaLatin}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = surat.nama,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${surat.jumlahAyat} ayat • ${surat.tempatTurun}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = surat.nama,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}