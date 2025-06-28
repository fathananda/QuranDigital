package com.fathi.qurandigital.composable.alquran

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.fathi.qurandigital.QuranRepository
import com.fathi.qurandigital.composable.murottal.MurottalScreen
import com.fathi.qurandigital.composable.prayertime.PrayerTimeScreen
import com.fathi.qurandigital.TabItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlQuranApp(repository: QuranRepository){
    var selectedTab by remember { mutableStateOf(0) }

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

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Al-Qur'an Digital")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.title)},
                        label = { Text(tab.title)},
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ){ innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when(selectedTab){
                0 -> QuranScreen()
                1 -> PrayerTimeScreen()
                2 -> MurottalScreen()
            }
        }

    }
}