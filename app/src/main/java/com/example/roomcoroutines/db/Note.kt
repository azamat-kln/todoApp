package com.example.roomcoroutines.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, @ColumnInfo(name = "note_title") val title: String,
    @ColumnInfo(name = "note_description") val description: String
) : Parcelable