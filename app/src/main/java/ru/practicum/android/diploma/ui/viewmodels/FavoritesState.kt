package ru.practicum.android.diploma.ui.viewmodels

import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyCard

sealed interface FavoritesState {
    data class Content(val vacancy: List<VacancyCard>) : FavoritesState
    data class IsEmpty(val isEmpty: Boolean) : FavoritesState
    data class ConnectionError(val connectionError: Boolean) : FavoritesState
}
