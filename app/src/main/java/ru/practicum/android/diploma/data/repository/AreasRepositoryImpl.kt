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
    override fun getAreas(): Flow<ApiResult<List<Area>>> = flow {
        emit(ApiResult.Loading)

        val response = networkClient.filterAreaRequest(FilterAreaRequest())
        if (response.resultCode == SUCCESS_CODE && response is FilterAreaResponse) {
            val domainTrees = response.results.map { it.toModel() }
            val flatList = mutableListOf<Area>()
            domainTrees.forEach { treeRoot ->
                flattenTree(treeRoot, flatList)
            }
            emit(ApiResult.Success(flatList))
        } else {
            emit(ApiResult.Error(response.resultCode))
        }
    }

    override fun getCountries(): Flow<ApiResult<List<Area>>> = flow {
        emit(ApiResult.Loading)

        val response = networkClient.filterAreaRequest(FilterAreaRequest())
        if (response.resultCode == SUCCESS_CODE && response is FilterAreaResponse) {
            val domainTrees = response.results.map { it.toModel() }
            val flatList = mutableListOf<Area>()
            domainTrees.forEach { treeRoot ->
                flattenTree(treeRoot, flatList)
            }
            val countries = flatList.filter { it.parentId == null }
            emit(ApiResult.Success(countries))
        } else {
            emit(ApiResult.Error(response.resultCode))
        }
    }

    private fun flattenTree(node: Area, result: MutableList<Area>) {
        result.add(node)
        node.areas.forEach { child ->
            flattenTree(child, result)
        }
    }

    companion object {
        private const val SUCCESS_CODE = 200
    }
}
