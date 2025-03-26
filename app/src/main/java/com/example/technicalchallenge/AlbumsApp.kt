package com.example.technicalchallenge

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.technicalchallenge.ui.album.AlbumsScreen
import com.example.technicalchallenge.ui.photo.PhotoDetailScreen
import com.example.technicalchallenge.ui.theme.TechnicalChallengeTheme

@Composable
fun AlbumsApp() {
    val navController = rememberNavController()
    TechnicalChallengeTheme {
        NavHost(navController = navController, startDestination = "albums") {
            composable("albums") {
                AlbumsScreen(albumsViewModel = hiltViewModel(), navController = navController)
            }

            composable(
                route = "photo_detail/{photoId}",
                arguments = listOf(navArgument("photoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val photoId = backStackEntry.arguments?.getInt("photoId")
                photoId?.let {
                    PhotoDetailScreen(
                        photoId = it,
                        viewModel = hiltViewModel(),
                        navController = navController
                    )
                }
            }
        }
    }
}
