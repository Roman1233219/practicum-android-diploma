package ru.practicum.android.diploma.presentation.viewmodels

import ru.practicum.android.diploma.domain.models.Area

sealed class FiltrationCountryState {
    object Loading : FiltrationCountryState()
    data class Success(val countries: List<Area>) : FiltrationCountryState()
    data class ConnectionError(val code: Int) : FiltrationCountryState()
    data class NotFoundError(val code: Int) : FiltrationCountryState()
    data class ServerError500(val code: Int) : FiltrationCountryState()
}
