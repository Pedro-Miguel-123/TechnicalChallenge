package com.example.technicalchallenge.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Dimensions(
    val space1x: Dp = 8.dp,
    val space2x: Dp = 16.dp,
    val space3x: Dp = 24.dp,
    val iconSize: Dp = 48.dp,
    val iconBoxSize: Dp = 80.dp,
)

private val smallestDimensions = Dimensions(
    iconSize = 32.dp,
    iconBoxSize = 56.dp
)

private val smallDimensions = Dimensions(
    iconSize = 40.dp,
    iconBoxSize = 64.dp
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
