package ru.practicum.android.diploma.presentation.filter_area

sealed interface AreaUiState {
    object Empty : AreaUiState

    data class Content(
        val country: AreaUi?,
        val region: AreaUi?
    ) : AreaUiState
}
