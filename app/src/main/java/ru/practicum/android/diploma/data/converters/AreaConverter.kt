package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.FilterAreaDto
import ru.practicum.android.diploma.data.dto.FilterAreaResponse
import ru.practicum.android.diploma.domain.models.Area

fun FilterAreaResponse.toModel(): List<Area> {
    return results.map { it.toModel() }
}

fun FilterAreaDto.toModel(): Area {
    return Area(
        id = id,
        name = name,
        parentId = parentId,
        areas = areas.map { it.toModel() }
    )
}
