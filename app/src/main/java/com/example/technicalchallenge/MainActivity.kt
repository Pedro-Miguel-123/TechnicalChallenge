package com.example.technicalchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.technicalchallenge.data.PhotoRepository
import com.example.technicalchallenge.data.PhotoRepositoryImpl
import com.example.technicalchallenge.data.api.RetrofitInstance
import com.example.technicalchallenge.data.db.PhotoDatabase
import com.example.technicalchallenge.data.net.NetworkMonitor
import com.example.technicalchallenge.data.net.NetworkMonitorImpl
import com.example.technicalchallenge.ui.theme.TechnicalChallengeTheme
import com.example.technicalchallenge.viewModels.PhotoViewModel
import com.example.technicalchallenge.viewModels.PhotoViewModelFactory
import com.example.technicalchallenge.views.MainScreen
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private lateinit var photoViewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG_MODE) {
            Timber.plant(Timber.DebugTree())
        }
        val db = PhotoDatabase.build(context = this)
        val networkMonitor : NetworkMonitor = NetworkMonitorImpl(this) {
            photoViewModel.fetchAndSavePhotos()
        }
        val apiService = RetrofitInstance.api
        val repository: PhotoRepository = PhotoRepositoryImpl(db.photoDao(), networkMonitor, apiService)
        val factory = PhotoViewModelFactory(repository)
        photoViewModel = ViewModelProvider(this, factory) [PhotoViewModel::class.java
        ]

        photoViewModel.fetchAndSavePhotos()
        enableEdgeToEdge()
        setContent {
            TechnicalChallengeTheme {
                MainScreen(photoViewModel)
            }
        }
    }
}