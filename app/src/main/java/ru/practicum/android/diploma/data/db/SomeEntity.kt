package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "some_table")
data class SomeEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int
)
