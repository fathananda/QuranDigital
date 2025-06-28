package com.fathi.qurandigital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fathi.qurandigital.composable.alquran.AlQuranApp
import com.fathi.qurandigital.ui.theme.QuranDigitalTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var repository: QuranRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuranDigitalTheme {
                AlQuranApp(repository = repository)
            }
        }
    }
}
