package com.example.technicalchallenge.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    val albumId: Int,
    @PrimaryKey val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
) {
    companion object {
        fun empty() = Photo(
                albumId = 0,
                id = 0,
                title= "",
                url = "",
                thumbnailUrl = ""
        )
    }
}
