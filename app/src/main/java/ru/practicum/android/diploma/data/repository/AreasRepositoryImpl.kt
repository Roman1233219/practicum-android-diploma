package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.converters.toModel
import ru.practicum.android.diploma.data.dto.FilterAreaRequest
import ru.practicum.android.diploma.data.dto.FilterAreaResponse
import ru.practicum.android.diploma.domain.api.AreasRepository
import ru.practicum.android.diploma.domain.models.ApiResult
import ru.practicum.android.diploma.domain.models.Area

class AreasRepositoryImpl(private var networkClient: NetworkClient) : AreasRepository {
    override fun getAreas(): Flow<ApiResult<Area>> = flow {
        emit(ApiResult.Loading)

        val response = networkClient.filterAreaRequest(FilterAreaRequest())
        if (response.resultCode == SUCCESS_CODE && response is FilterAreaResponse) {
            emit(ApiResult.Success(response.results.toModel()))
        } else {
            emit(ApiResult.Error(response.resultCode))
        }
    }

    companion object {
        private const val SUCCESS_CODE = 200
    }
}
