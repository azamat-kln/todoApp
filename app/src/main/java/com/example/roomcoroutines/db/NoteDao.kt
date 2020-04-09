package com.example.roomcoroutines.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("select * from note_table")
    fun getWords(): LiveData<List<Note>>

    @Query("delete from note_table")
    suspend fun deleteAll()

}