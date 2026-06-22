package ru.practicum.android.diploma.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.ApiResult
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val vacancyRepository: VacanciesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApiResult<VacanciesSearchResult>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<VacanciesSearchResult>> = _uiState.asStateFlow()
    private val mockVacanciesList: MutableList<VacancyCard> = mutableListOf()
    private var searchJob: Job? = null
    private val vacancySearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changeText ->
            searchVacancies(changeText)
        }

    init {
        viewModelScope.launch(Dispatchers.Main) {
            //
        }
    }

    fun searchVacancies(query: String): Flow<ApiResult<VacanciesSearchResult>> = flow {
        emit(ApiResult.Loading)
        delay(500)

        try {
            // Здесь должен быть реальный сетевой вызов.
            // Для примера используем мок-данные.
            val result = vacancyRepository.searchVacancies(
                query = query, page = 1//??
            )
                // val result = generateMockVacanciesSearchResult()
                .collect { result -> ApiResult.Success(result) }
        } catch (e: Exception) {
            emit(ApiResult.Error(500))
        }

    }

    fun performSearch(query: String) {
        viewModelScope.launch {
            searchVacancies(query).collect { result ->
                _uiState.value = result
            }
        }
    }

    // --- Генератор мок-данных ---
    private fun generateMockVacanciesSearchResult(): VacanciesSearchResult {
        mockVacanciesList.add(
            VacancyCard(
                "1",
                "Какая-то вакансия 1",
                "Яндекс",
                "Москва",
                80000,
                100000,
                "RUB",
                ""
            )
        )
        mockVacanciesList.add(
            VacancyCard(
                "2",
                "Какая-то вакансия 2",
                "Яндекс",
                "Москва",
                90000,
                130000,
                "RUB",
                ""
            )
        )
        mockVacanciesList.add(
            VacancyCard(
                "3",
                "Какая-то вакансия 3",
                "ВК",
                "Москва",
                90000,
                130000,
                "RUB",
                ""
            )
        )
        mockVacanciesList.add(
            VacancyCard(
                "4",
                "Какая-то вакансия 4",
                "Татнефть",
                "Казань",
                70000,
                90000,
                "RUB",
                ""
            )
        )

        return VacanciesSearchResult(
            vacancies = mockVacanciesList,
            vacanciesFound = 4,
            pagesCount = 1,
            currentPage = 1
        )
    }

    private fun renderState(newState: SearchState) {
        //
    }

    fun cancelSearch() {
        searchJob?.cancel()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
