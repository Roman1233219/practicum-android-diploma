package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterSettings

class FilterSettingsInteractorImpl(
    private val repository: FilterSettingsRepository
) : FilterSettingsInteractor {

    override fun getFilterSettings(): FilterSettings {
        return repository.getFilterSettings()
    }

    override suspend fun saveFilterSettings(settings: FilterSettings) {
        repository.saveFilterSettings(settings)
    }

    override fun saveCountry(country: Area) {
        repository.saveCountry(country)
    }

    override fun clearFilterSettings() {
        repository.clearFilterSettings()
    }

    override fun getFilterFlow(): Flow<FilterSettings> {
        return repository.getFilterFlow()
    }

    override fun getTempFilterFlow(): Flow<FilterSettings> {
        return repository.getTempFilterFlow()
    }

    override suspend fun saveTempFilter(settings: FilterSettings) {
        repository.saveTempFilter(settings)
    }

    override suspend fun applyTempFilter() {
        repository.applyTempFilter()
    }

    override suspend fun initTempFilter() {
        repository.initTempFilter()
    }

    override suspend fun clearTempFilter() {
        repository.clearTempFilter()
    }
}
