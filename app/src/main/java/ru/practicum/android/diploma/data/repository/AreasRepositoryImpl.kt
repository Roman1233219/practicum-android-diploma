package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.converters.toModel
import ru.practicum.android.diploma.data.network.PracticumApiService
import ru.practicum.android.diploma.data.network.models.HttpErrorType.Companion.CLIENT_ERROR_START
import ru.practicum.android.diploma.domain.api.AreasRepository
import ru.practicum.android.diploma.domain.models.ApiResult
import ru.practicum.android.diploma.domain.models.Area

class AreasRepositoryImpl(private val apiService: PracticumApiService,) : AreasRepository {
    override fun getAreas(): Flow<ApiResult<List<Area>>> = flow {
        emit(ApiResult.Loading)

        val response = apiService.getAreas()
        if (response.isNotEmpty()) {
            val rootDtos = response
            val domainTrees = rootDtos.map { it.toModel() }
            val flatList = mutableListOf<Area>()
            domainTrees.forEach { treeRoot ->
                flattenTree(treeRoot, flatList)
            }

            emit(ApiResult.Success(flatList))
        } else {
            emit(ApiResult.Error(CLIENT_ERROR_START))
        }
    }

    override fun getCountries(): Flow<ApiResult<List<Area>>> = flow {
        emit(ApiResult.Loading)

        val response = apiService.getAreas()
        if (response.isNotEmpty()) {
            val rootDtos = response
            val domainTrees = rootDtos.map { it.toModel() }
            val flatList = mutableListOf<Area>()
            domainTrees.forEach { treeRoot ->
                flattenTree(treeRoot, flatList)
            }

            val countries = flatList.filter { it.parentId == null }
            emit(ApiResult.Success(countries))
        } else {
            emit(ApiResult.Error(CLIENT_ERROR_START))
        }
    }

    private fun flattenTree(node: Area, result: MutableList<Area>) {
        result.add(node)
        node.areas.forEach { child ->
            flattenTree(child, result)
        }
    }
}
