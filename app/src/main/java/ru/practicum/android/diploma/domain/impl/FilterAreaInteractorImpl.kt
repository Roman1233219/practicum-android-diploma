package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.FilterAreaInteractor
import ru.practicum.android.diploma.domain.api.FilterAreaRepository
import ru.practicum.android.diploma.domain.models.Area

class FilterAreaInteractorImpl(private val repository: FilterAreaRepository) : FilterAreaInteractor {
    override fun getAreas(): Flow<List<Area>> {
        return repository.getAreas()
    }
}
