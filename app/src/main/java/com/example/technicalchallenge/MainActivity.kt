package com.example.technicalchallenge

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.technicalchallenge.data.PhotoRepository
import com.example.technicalchallenge.data.db.PhotoDatabase
import com.example.technicalchallenge.ui.theme.TechnicalChallengeTheme
import com.example.technicalchallenge.viewModels.PhotoViewModel

class MainActivity : ComponentActivity() {
    private lateinit var photoViewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = PhotoDatabase.getDatabase(this)
        val repository = PhotoRepository(db.photoDao())
        photoViewModel = ViewModelProvider(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PhotoViewModel(repository) as T
            }
        })[PhotoViewModel::class.java]

        photoViewModel.fetchAndSavePhotos()
        photoViewModel.getPhotosFromDb { photos ->
            Log.d("MainActivity", "Fetched photos: $photos")
        }
        enableEdgeToEdge()
        setContent {
            TechnicalChallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TechnicalChallengeTheme {
        Greeting("Android")
    }
}