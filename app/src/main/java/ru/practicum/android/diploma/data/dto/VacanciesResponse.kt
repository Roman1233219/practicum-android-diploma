package ru.practicum.android.diploma.data.dto

data class VacanciesResponse(val found: Long, val pages: Long, val page: Long, val items: List<VacancyDto>) : Response()
