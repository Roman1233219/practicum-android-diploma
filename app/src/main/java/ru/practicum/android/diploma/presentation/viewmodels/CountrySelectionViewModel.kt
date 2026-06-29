package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.practicum.android.diploma.data.network.models.HttpErrorType
import ru.practicum.android.diploma.data.network.models.toHttpErrorType
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
                    FiltrationCountryState.Success(result.data)
                }
                is ApiResult.Error -> handleError(result.httpCode)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FiltrationCountryState.Loading
        )

    private fun handleError(httpCode: Int): FiltrationCountryState {
        when (httpCode.toHttpErrorType()) {
            HttpErrorType.NETWORK,
            HttpErrorType.UNKNOWN -> {
                return FiltrationCountryState.ConnectionError(httpCode)
            }
            HttpErrorType.CLIENT -> {
                return FiltrationCountryState.NotFoundError(httpCode)
            }
            HttpErrorType.SERVER -> {
                return FiltrationCountryState.ServerError500(httpCode)
            }
        }
    }

    fun onCountrySelected(country: Area) {
        repository.saveCountry(country)
    }
}
