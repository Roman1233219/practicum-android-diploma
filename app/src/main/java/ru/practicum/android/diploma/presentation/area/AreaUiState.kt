package ru.practicum.android.diploma.presentation.area

sealed interface AreaUiState{
    object Initial : AreaUiState
    data class Empty(val message: String) : AreaUiState
    data class Error(val message: String) : AreaUiState
    data class Content(val areas: List<AreaUi>) : AreaUiState
}
