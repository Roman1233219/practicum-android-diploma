package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.converters.toModel
import ru.practicum.android.diploma.data.dto.FilterAreaRequest
import ru.practicum.android.diploma.data.dto.FilterAreaResponse
import ru.practicum.android.diploma.domain.api.FilterAreaRepository
import ru.practicum.android.diploma.domain.models.Area

class FilterAreaRepositoryImpl(private val networkClient: NetworkClient) : FilterAreaRepository {
    override fun getAreas(): Flow<List<Area>> = flow {
        val response = networkClient.filterAreaRequest(FilterAreaRequest())
        if (response.resultCode == SUCCESS_CODE && response is FilterAreaResponse) {
            emit(response.toModel())
        } else {
            emit(emptyList())
        }
    }

    companion object {
        private const val SUCCESS_CODE = 200
    }
}
