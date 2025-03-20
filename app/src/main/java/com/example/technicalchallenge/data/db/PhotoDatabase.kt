package com.example.technicalchallenge.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Photo::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao

    companion object {
        private const val databaseName = "photo_database"
        fun build(context: Context) =
            Room.databaseBuilder(context, PhotoDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .build()
    }
}