package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.converters.toModel
import ru.practicum.android.diploma.domain.models.ApiResult
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyRepositoryImpl(private var networkClient: NetworkClient): VacanciesRepository {
    override fun searchVacancies(
        query: String,
        page: Int
    ): Flow<ApiResult<List<Vacancy>>> = flow {
        emit(ApiResult.Loading)
        val data = networkClient.searchVacancies(VacanciesRequest(query, page))

        if (data.resultCode == 200 && data is VacanciesResponse)
            emit(ApiResult.Success(data.items.map { t->t.toModel() }))
        else
            emit(ApiResult.Error(data.resultCode))
    }
}
