package ru.practicum.android.diploma.domain.impl

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

    override fun saveFilterSettings(settings: FilterSettings) {
        repository.saveFilterSettings(settings)
    }

    override fun saveCountry(country: Area) {
        repository.saveCountry(country)
    }

    override fun clearFilterSettings() {
        repository.clearFilterSettings()
    }
}
