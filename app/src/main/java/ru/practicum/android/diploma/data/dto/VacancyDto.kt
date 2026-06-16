package ru.practicum.android.diploma.data.dto

import kotlin.uuid.Uuid

data class VacancyDto(val id: String, val name: String, val company: String, val city: String, val salary: SalaryDto, val logo: String)
