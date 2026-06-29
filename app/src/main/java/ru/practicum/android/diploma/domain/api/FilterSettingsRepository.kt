package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterSettings

interface FilterSettingsRepository {
    fun getFilterSettings(): FilterSettings
    fun saveFilterSettings(settings: FilterSettings)
    fun saveCountry(country: Area)
    fun clearFilterSettings()
}
