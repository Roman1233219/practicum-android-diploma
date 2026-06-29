package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.entity.FilterSettingsEntity
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Database(version = 3, exportSchema = true, entities = [VacancyEntity::class, FilterSettingsEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun vacancyDao(): VacancyDao
    abstract fun filterSettingsDao(): FilterSettingsDao
}
