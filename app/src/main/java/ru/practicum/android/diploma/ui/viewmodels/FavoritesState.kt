package ru.practicum.android.diploma.ui.viewmodels

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface FavoritesState {
    object Loading : FavoritesState
    data class Content(val vacancies: List<Vacancy>) : FavoritesState
    data class Error(val message: String) : FavoritesState
    object Empty: FavoritesState
}
