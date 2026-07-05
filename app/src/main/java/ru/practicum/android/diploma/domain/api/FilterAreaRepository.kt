package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Area

interface FilterAreaRepository {
    fun getAreas(): Flow<List<Area>>
}
