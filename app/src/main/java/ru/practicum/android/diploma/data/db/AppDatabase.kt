package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.dao.SomeDao

@Database(version = 1, entities = [SomeEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun someDao(): SomeDao

}
