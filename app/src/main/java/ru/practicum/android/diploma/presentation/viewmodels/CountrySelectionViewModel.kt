package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.practicum.android.diploma.domain.api.AreasInteractor
import ru.practicum.android.diploma.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.domain.models.ApiResult
import ru.practicum.android.diploma.domain.models.Area

class CountrySelectionViewModel(
    private val interactor: AreasInteractor,
    private val repository: FilterSettingsRepository,
) : ViewModel() {

    val uiState: StateFlow<FiltrationCountryState> = interactor.getCountries()
        .map { result ->
            when (result) {
                is ApiResult.Loading -> FiltrationCountryState.Loading

                is ApiResult.Success -> {
                    val countries = result.data.filter { it.parentId == null }
                    FiltrationCountryState.Success(countries)
                }

                is ApiResult.Error -> FiltrationCountryState.Error(result.httpCode)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FiltrationCountryState.Loading
        )

    fun onCountrySelected(country: Area) {
        val currentSettings = repository.getFilterSettings()
        val newSettings = currentSettings.copy(
            countryId = country.id.toString(),
            countryName = country.name
        )

        repository.saveFilterSettings(newSettings)
    }
}
