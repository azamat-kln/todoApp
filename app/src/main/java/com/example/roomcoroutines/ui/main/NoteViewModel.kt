package com.example.roomcoroutines.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.roomcoroutines.db.Note
import com.example.roomcoroutines.repository.Repository
import com.example.roomcoroutines.db.WordRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : ViewModel() {

    private val repository: Repository

    // LiveData gives us updated words when they change.So we have to just observe
    val allWords: LiveData<List<Note>>

    private val job = Job()

    private val mainScope = CoroutineScope(Dispatchers.Main + job)

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, mainScope).noteDao
        repository = Repository(wordsDao)
        allWords = repository.allWords
    }

    fun insert(note: Note) =
        mainScope.launch {
            repository.insert(note)
        }


    fun update(note: Note) =
        mainScope.launch {
            repository.update(note)
        }

    fun deleteNote(note: Note) =
        mainScope.launch {
            repository.delete(note)
        }

    fun clear() = mainScope.launch {
        repository.deleteAll()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}