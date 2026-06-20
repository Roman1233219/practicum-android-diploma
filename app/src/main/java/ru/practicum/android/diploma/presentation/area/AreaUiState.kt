package ru.practicum.android.diploma.presentation.area

sealed interface AreaUiState{
    object Initial : AreaUiState
    data class Empty(val messageRes: Int) : AreaUiState
    data class Error(val messageRes: Int) : AreaUiState
    data class Content(val areas: List<AreaUi>) : AreaUiState
}
