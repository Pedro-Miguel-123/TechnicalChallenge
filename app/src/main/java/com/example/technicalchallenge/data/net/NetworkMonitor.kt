package com.example.technicalchallenge.data.net

interface NetworkMonitor {
    fun register()
    fun setOnNetworkAvailable(callback: () -> Unit)
}
