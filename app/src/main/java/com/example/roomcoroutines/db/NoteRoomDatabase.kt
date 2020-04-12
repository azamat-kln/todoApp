package com.example.roomcoroutines.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 23, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

}

