package ru.practicum.android.diploma.presentation.filterarea

sealed interface RegionUiState {
    object Initial : RegionUiState
    data class Empty(val messageRes: Int) : RegionUiState
    data class Error(val messageRes: Int) : RegionUiState
    data class Content(val regions: List<AreaUi>) : RegionUiState
}
