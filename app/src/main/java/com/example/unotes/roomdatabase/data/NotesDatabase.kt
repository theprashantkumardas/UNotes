package com.example.unotes.roomdatabase.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database( entities = [Note::class] , version = 1)
@TypeConverters(Converters::class)
abstract class NotesDatabase: RoomDatabase() {
    abstract val dao: NoteDao
}