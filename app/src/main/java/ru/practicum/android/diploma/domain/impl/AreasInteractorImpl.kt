package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.AreasInteractor
import ru.practicum.android.diploma.domain.api.AreasRepository
import ru.practicum.android.diploma.domain.models.ApiResult
import ru.practicum.android.diploma.domain.models.Area

class AreasInteractorImpl(private val repository: AreasRepository): AreasInteractor {
    override fun getAreas(): Flow<ApiResult<Area>> {
        return repository.getAreas()
    }
}
