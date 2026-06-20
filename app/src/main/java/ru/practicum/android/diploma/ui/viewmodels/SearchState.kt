package ru.practicum.android.diploma.ui.viewmodels

sealed interface SearchState {
    data class SearchText(val text: String) : SearchState
    data class VacanciesCount(val vacanciesCount: Int) : SearchState
    data object IsLoadingNextPage : SearchState
    data object IsLoading : SearchState
    data class QueryIsEmpty(val isEmpty: Boolean) : SearchState
    data class ConnectionError(val replaceVacancyList: Boolean) : SearchState
    data class NotFoundError(val replaceVacancyList: Boolean) : SearchState
    data class ServerError500(val replaceVacancyList: Boolean) : SearchState
}
