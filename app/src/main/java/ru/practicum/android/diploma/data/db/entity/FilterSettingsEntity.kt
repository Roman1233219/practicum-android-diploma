package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filter_settings_table")
data class FilterSettingsEntity(
    @PrimaryKey
    val filterId: Int, // Используем константы для основного и временного фильтров
    val countryId: String? = null,
    val countryName: String? = null,
    val regionId: String? = null,
    val regionName: String? = null,
    val industryId: String? = null,
    val industryName: String? = null,
    val expectedSalary: Int? = null,
    val notShowWithoutSalary: Boolean = false
)
