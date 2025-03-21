package com.example.technicalchallenge.di

import android.content.Context
import com.example.technicalchallenge.data.AlbumRepository
import com.example.technicalchallenge.data.AlbumRepositoryImpl
import com.example.technicalchallenge.data.api.APIService
import com.example.technicalchallenge.data.local.PhotoDao
import com.example.technicalchallenge.data.network.NetworkMonitor
import com.example.technicalchallenge.data.network.NetworkMonitorImpl
import com.example.technicalchallenge.util.Constants.BASE_URL
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

    @Provides
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor {
        return NetworkMonitorImpl(context) {
        }
    }

    @Provides
    fun provideApiService(): APIService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }

    @Provides
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Provides
    fun provideRepository(
        photoDao: PhotoDao,
        networkMonitor: NetworkMonitor,
        apiService: APIService,
        coroutineScope: CoroutineScope
    ): AlbumRepository {
        return AlbumRepositoryImpl(photoDao, networkMonitor, apiService, coroutineScope)
    }
}
