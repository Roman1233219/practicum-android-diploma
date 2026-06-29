package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.converters.toModel
import ru.practicum.android.diploma.data.dto.FilterIndustriesRequest
import ru.practicum.android.diploma.data.dto.FilterIndustriesResponse
import ru.practicum.android.diploma.domain.api.FilterIndustriesRepository
import ru.practicum.android.diploma.domain.models.ApiResult
import ru.practicum.android.diploma.domain.models.Industry

class FilterIndustriesRepositoryImpl(private var networkClient: NetworkClient) : FilterIndustriesRepository {
    override fun getIndustries(): Flow<ApiResult<List<Industry>>> = flow {
        val request = FilterIndustriesRequest()
        val data = networkClient.filterIndustryRequest(request)
        if (data.resultCode == SUCCESS_CODE && data is FilterIndustriesResponse) {
            emit(ApiResult.Success(data.toModel()))
        } else {
            emit(ApiResult.Error(data.resultCode))
        }
    }

    companion object {
        private const val SUCCESS_CODE = 200
    }
}
