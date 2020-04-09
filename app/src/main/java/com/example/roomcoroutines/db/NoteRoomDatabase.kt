package com.example.roomcoroutines.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(entities = [Note::class], version = 23, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {

        @Volatile
        var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context, mainScope: CoroutineScope): WordRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                ).addCallback(NoteDatabaseCallback(mainScope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}

private class NoteDatabaseCallback(private val scope: CoroutineScope) :
    RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        WordRoomDatabase.INSTANCE?.let { database ->
            scope.launch {
                populateDatabase(database.noteDao)
            }
        }
    }

    private suspend fun populateDatabase(noteDao: NoteDao) {
        withContext(Dispatchers.IO) {
            // Delete all content here.
            noteDao.deleteAll()

            // Add sample words.
            var word = Note(title = "Hello", description = "hello description")
            noteDao.insert(word)
            word = Note(title = "World", description = "world description")
            noteDao.insert(word)
            // TODO: Add your own words!
        }

    }
}