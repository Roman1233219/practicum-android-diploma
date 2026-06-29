package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.ApiResult
import ru.practicum.android.diploma.domain.models.Area

interface AreasRepository {
    fun getAreas(): Flow<ApiResult<List<Area>>>
    fun getCountries(): Flow<ApiResult<List<Area>>>
}
