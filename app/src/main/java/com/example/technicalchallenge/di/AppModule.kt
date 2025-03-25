package com.example.technicalchallenge.di

import android.content.Context
import com.example.technicalchallenge.data.AlbumRepository
import com.example.technicalchallenge.data.AlbumRepositoryImpl
import com.example.technicalchallenge.data.api.LebonCoinAPIService
import com.example.technicalchallenge.data.local.PhotoDao
import com.example.technicalchallenge.data.network.NetworkMonitor
import com.example.technicalchallenge.data.network.NetworkMonitorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    const val BASE_URL= "https://static.leboncoin.fr/img/shared/"

    @Provides
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor {
        return NetworkMonitorImpl(context) {
        }
    }

    @Provides
    fun provideApiService(): LebonCoinAPIService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LebonCoinAPIService::class.java)
    }

    @Provides
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Provides
    fun provideRepository(
        photoDao: PhotoDao,
        networkMonitor: NetworkMonitor,
        lebonCoinApiService: LebonCoinAPIService,
        coroutineScope: CoroutineScope
    ): AlbumRepository {
        return AlbumRepositoryImpl(photoDao, networkMonitor, lebonCoinApiService, coroutineScope)
    }
}
