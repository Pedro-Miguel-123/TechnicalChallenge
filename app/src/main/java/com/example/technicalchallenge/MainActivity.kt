package com.example.technicalchallenge

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.technicalchallenge.data.Photo
import com.example.technicalchallenge.data.PhotoRepository
import com.example.technicalchallenge.data.db.PhotoDatabase
import com.example.technicalchallenge.ui.theme.TechnicalChallengeTheme
import com.example.technicalchallenge.viewModels.PhotoViewModel
import com.example.technicalchallenge.views.MainScreen
import com.example.technicalchallenge.views.MyScrollableList

class MainActivity : ComponentActivity() {
    private lateinit var photoViewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = PhotoDatabase.build(context = this)
        val repository = PhotoRepository(db.photoDao(), context = applicationContext)
        photoViewModel = ViewModelProvider(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PhotoViewModel(repository) as T
            }
        })[PhotoViewModel::class.java]

        photoViewModel.fetchAndSavePhotos()
        enableEdgeToEdge()
        setContent {
            MainScreen(photoViewModel)
        }
    }
}