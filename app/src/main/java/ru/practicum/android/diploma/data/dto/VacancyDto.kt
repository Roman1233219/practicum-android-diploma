package ru.practicum.android.diploma.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VacancyDto (
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyCardDto>
) : Parcelable
