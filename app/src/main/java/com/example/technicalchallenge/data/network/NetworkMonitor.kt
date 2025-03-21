package com.example.technicalchallenge.data.network

interface NetworkMonitor {
    fun register()
    fun setOnNetworkAvailable(callback: () -> Unit)
}
