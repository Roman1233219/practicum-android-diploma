package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.FilterAreaDto
import ru.practicum.android.diploma.domain.models.Area

fun FilterAreaDto.toModel(): Area = Area(
    id = this.id,
    name = this.name,
    parentId = this.parentId,
    areas = this.areas.map { it.toModel() }.toMutableList()
)

fun Area.toDto(): FilterAreaDto = FilterAreaDto(
    id = this.id,
    name = this.name,
    parentId = this.parentId,
    areas = this.areas.map { it.toDto() }
)
