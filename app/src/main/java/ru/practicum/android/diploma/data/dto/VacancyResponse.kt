package ru.practicum.android.diploma.data.dto

class VacancyResponse (val resultCount: Int,
                       val area: String?,
                       val industry: String?,
                       val text: String?,
                       val salary: String?,
                       val page: String?,
                       val results: VacancyDto) : Response()
