package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterSettings

interface FilterSettingsInteractor {
    fun getFilterSettings(): FilterSettings
    suspend fun saveFilterSettings(settings: FilterSettings)
    fun saveCountry(country: Area)
    fun clearFilterSettings()

    fun getFilterFlow(): Flow<FilterSettings>
    fun getTempFilterFlow(): Flow<FilterSettings>
    suspend fun saveTempFilter(settings: FilterSettings)
    suspend fun applyTempFilter()
    suspend fun initTempFilter()
    suspend fun clearTempFilter()
}
