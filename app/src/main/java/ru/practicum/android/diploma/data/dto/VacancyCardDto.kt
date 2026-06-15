package ru.practicum.android.diploma.data.dto

import android.os.Parcelable

data class VacancyCardDto (
    val id: Int,
    val name: String,
    val company: String?,
    val city: String?,
    val salary: VacancyCardSalaryDto?,
    val logo: String?
) : Parcelable
