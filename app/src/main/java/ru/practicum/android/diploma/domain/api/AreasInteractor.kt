package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.ApiResult
import ru.practicum.android.diploma.domain.models.Area

interface AreasInteractor {
    fun getAreas(): Flow<ApiResult<Area>>
}
