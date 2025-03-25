package com.example.technicalchallenge.util.network

interface NetworkMonitor {
    fun register()
    fun setOnNetworkAvailable(callback: (() -> Unit)?)
}
