package com.example.roomcoroutines.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomcoroutines.db.Note
import com.example.roomcoroutines.db.NoteDao
import com.example.roomcoroutines.db.WordRoomDatabase
import com.example.roomcoroutines.repository.NoteRepository
import com.example.roomcoroutines.ui.main.NoteViewModel
import kotlinx.coroutines.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { NoteViewModel(get()) }
}

val repositoryModule = module {
    single { NoteRepository(get()) }
}

val noteDaoModule = module {

    fun provideNoteDao(database: WordRoomDatabase): NoteDao {
        return database.noteDao
    }

    single { provideNoteDao(get()) }
}

val databaseModule = module {

    single {
        Room.databaseBuilder(androidApplication(), WordRoomDatabase::class.java, "note_db")
            .addCallback(get())
            .fallbackToDestructiveMigration()
            .build()
    }

}

val roomCallBackModule = module {

    single<RoomDatabase.Callback> {
        object : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                get<CoroutineScope>().launch {
                    populateDb(get())
                }
            }

            private suspend fun populateDb(dao: NoteDao) {
                withContext(Dispatchers.IO) {
                    dao.deleteAll()

                    val word = Note(title = "Hello", description = "hello description")
                    dao.insert(word)
                }
            }
        }
    }

}

val coroutineScopeModule = module {

    val mainScope = CoroutineScope(Dispatchers.Main)

    single { mainScope }
}


