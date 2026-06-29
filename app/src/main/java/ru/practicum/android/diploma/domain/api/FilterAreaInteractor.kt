package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Area

interface FilterAreaInteractor {
    fun getAreas(): Flow<List<Area>>
}
