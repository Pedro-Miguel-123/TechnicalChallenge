package com.example.technicalchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.technicalchallenge.ui.theme.TechnicalChallengeTheme
import com.example.technicalchallenge.views.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG_MODE) {
            Timber.plant(Timber.DebugTree())
        }
        enableEdgeToEdge()
        setContent {
            TechnicalChallengeTheme {
                MainScreen()
            }
        }
    }
}
