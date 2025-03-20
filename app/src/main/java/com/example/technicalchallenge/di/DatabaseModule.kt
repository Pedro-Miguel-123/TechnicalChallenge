package com.example.technicalchallenge.di

import android.content.Context
import androidx.room.Room
import com.example.technicalchallenge.data.db.PhotoDao
import com.example.technicalchallenge.data.db.PhotoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val databaseName = "photo_database"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PhotoDatabase {
        return  Room.databaseBuilder(
            context.applicationContext,
            PhotoDatabase::class.java,
            databaseName
        ).fallbackToDestructiveMigration()
            .build()

    }

    @Provides
    fun provideDao(database: PhotoDatabase): PhotoDao = database.photoDao()
}
