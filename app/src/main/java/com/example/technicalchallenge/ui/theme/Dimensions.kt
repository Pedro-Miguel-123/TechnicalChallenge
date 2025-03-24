package com.example.technicalchallenge.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Dimensions (
    val space05x: Dp = 4.dp,
    val space1x: Dp = 8.dp,
    val space2x: Dp = 16.dp,
    val space15x: Dp = 120.dp,
    val space18x: Dp = 144.dp,
    val space40x: Dp = 320.dp
)

private val smallestDimensions = Dimensions(
    space18x = 128.dp,
    space40x = 240.dp,
    space15x = 80.dp
)

private val smallDimensions = Dimensions(
    space18x = 128.dp,
    space40x = 280.dp,
    space15x = 100.dp
)

private val normalDimensions = Dimensions()

val dimensions: Dimensions
    @Composable
    get() {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenHeightDp <= 580 -> smallestDimensions
            configuration.screenHeightDp <= 700 -> smallDimensions
            else -> normalDimensions
        }
    }
