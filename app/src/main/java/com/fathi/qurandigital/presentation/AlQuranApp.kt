package com.fathi.qurandigital.presentation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fathi.qurandigital.features.murottal.ui.MurottalScreen
import com.fathi.qurandigital.features.prayertime.ui.PrayerTimeScreen
import com.fathi.qurandigital.core.ui.TabItem
import com.fathi.qurandigital.features.quran.ui.QuranScreen
import com.fathi.qurandigital.features.quran.ui.SuratDetailScreen
import com.fathi.qurandigital.features.murottal.viewmodel.MurottalViewModel
import com.fathi.qurandigital.features.prayertime.viewmodel.PrayerTimeViewModel
import com.fathi.qurandigital.features.quran.viewmodel.QuranViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlQuranApp(
    quranViewModel: QuranViewModel,
    prayerTimeViewModel: PrayerTimeViewModel,
    murottalViewModel: MurottalViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }
    val quranUiState by quranViewModel.uiState.collectAsState()

    val tabs = listOf(
        TabItem("Al-Quran", Icons.AutoMirrored.Filled.MenuBook),
        TabItem("Jadwal Sholat", Icons.Default.Schedule),
        TabItem("Murottal", Icons.Default.PlayCircle)
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var allPermissionsGranted = true
        permissions.entries.forEach {
            if (!it.value) {
                allPermissionsGranted = false
                return@forEach
            }
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    if (quranUiState.selectedSurat != null) {
        SuratDetailScreen(
            surat = quranUiState.selectedSurat!!,
            onBackClick = { quranViewModel.clearSelectedSurat() }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Al-Qur'an Digital",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1B5E20),
                        titleContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    tabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    tab.icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    tab.title,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF2E7D32),
                                selectedTextColor = Color(0xFF2E7D32),
                                indicatorColor = Color(0xFF2E7D32).copy(alpha = 0.12f)
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when(selectedTab) {
                    0 -> QuranScreen(viewModel = quranViewModel)
                    1 -> PrayerTimeScreen(viewModel = prayerTimeViewModel)
                    2 -> MurottalScreen(viewModel = murottalViewModel)
                }
            }
        }
    }
}