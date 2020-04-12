package com.example.roomcoroutines.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.roomcoroutines.db.Note
import com.example.roomcoroutines.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    // LiveData gives us updated words when they change.So we have to just observe
    val allWords: LiveData<List<Note>> = noteRepository.allWords

    private val job = Job()

    private val mainVmScope = CoroutineScope(Dispatchers.Main + job)

    fun insert(note: Note) =
        mainVmScope.launch {
            noteRepository.insert(note)
        }

    fun update(note: Note) =
        mainVmScope.launch {
            noteRepository.update(note)
        }

    fun deleteNote(note: Note) =
        mainVmScope.launch {
            noteRepository.delete(note)
        }

    fun clear() = mainVmScope.launch {
        noteRepository.deleteAll()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}