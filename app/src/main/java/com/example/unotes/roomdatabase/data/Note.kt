package com.example.unotes.roomdatabase.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val description: String,
    val dateAdded: Long,
    val timestamp: Long // Add timestamp field to store the creation date

)
