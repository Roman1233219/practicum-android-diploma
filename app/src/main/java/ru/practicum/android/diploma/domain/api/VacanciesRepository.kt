package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.ApiResult
import ru.practicum.android.diploma.domain.models.Vacancy

interface VacanciesRepository {
    fun searchVacancies(query: String, page: Int) : Flow<ApiResult<List<Vacancy>>>
}
