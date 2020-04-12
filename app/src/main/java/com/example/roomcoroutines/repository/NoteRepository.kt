package com.example.roomcoroutines.repository

import androidx.lifecycle.LiveData
import com.example.roomcoroutines.db.Note
import com.example.roomcoroutines.db.NoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDao: NoteDao) {

    // this query executes on a background thread
    val allWords: LiveData<List<Note>> = noteDao.getWords()

    suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            noteDao.deleteAll()
        }
    }

    suspend fun delete(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }

    suspend fun update(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.updateNote(note)
        }
    }

}