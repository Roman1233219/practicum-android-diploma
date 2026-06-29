package ru.practicum.android.diploma.presentation.`filter-area`

sealed interface CountryUiState {
    object Loading : CountryUiState
    data class Content(val countries: List<AreaUi>) : CountryUiState
    data class Error(val messageRes: Int) : CountryUiState
}
