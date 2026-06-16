package ru.practicum.android.diploma.domain.converters

import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.models.Vacancy

fun VacancyDto.toModel(): Vacancy = Vacancy(
    this.id,
    this.name,
    this.company,
    this.city,
    this.salary?.toModel(),
    this.logo
)

fun Vacancy.toDto(): VacancyDto = VacancyDto(
    this.id,
    this.name,
    this.company,
    this.city,
    this.salary?.toDto(),
    this.logo
)
