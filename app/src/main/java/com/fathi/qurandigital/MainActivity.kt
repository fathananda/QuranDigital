package com.fathi.qurandigital

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.fathi.qurandigital.composable.alquran.AlQuranApp
import com.fathi.qurandigital.ui.theme.QuranDigitalTheme
import com.fathi.qurandigital.viewmodel.MurottalViewModel
import com.fathi.qurandigital.viewmodel.PrayerTimeViewModel
import com.fathi.qurandigital.viewmodel.QuranViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val quranViewModel: QuranViewModel by viewModels()
    private val prayerTimeViewModel: PrayerTimeViewModel by viewModels()
    private val murottalViewModel: MurottalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuranDigitalTheme {
                // Request permissions
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    // Handle permission results
                    val allGranted = permissions.values.all { it }
                    if (allGranted) {
                        // Permissions granted, continue with app functionality
                    }
                }

                LaunchedEffect(Unit) {
                    // Check and request permissions
                    val permissions = mutableListOf<String>()

                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.INTERNET
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissions.add(Manifest.permission.INTERNET)
                    }

                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_NETWORK_STATE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE)
                    }

                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }

                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                    }

                    if (permissions.isNotEmpty()) {
                        permissionLauncher.launch(permissions.toTypedArray())
                    }
                }

                AlQuranApp(
                    quranViewModel = quranViewModel,
                    prayerTimeViewModel = prayerTimeViewModel,
                    murottalViewModel = murottalViewModel
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // ViewModels will be automatically cleared by Hilt
    }
}
