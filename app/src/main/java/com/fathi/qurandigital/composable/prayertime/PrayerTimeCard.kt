package com.fathi.qurandigital.composable.prayertime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fathi.qurandigital.PrayerTime

@Composable
fun PrayerTimeCard(
    prayerTime: PrayerTime,
    isNext: Boolean = false
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isNext) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isNext)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = prayerTime.name,
                    style = if (isNext)
                        MaterialTheme.typography.titleMedium
                    else
                        MaterialTheme.typography.bodyLarge
                )
                if (isNext) {
                    Text(
                        text = "Sholat Selanjutnya",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                text = prayerTime.time,
                style = MaterialTheme.typography.titleLarge,
                color = if (isNext)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}