package ru.practicum.android.diploma.presentation.`filter-area`

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Временный класс необходимый для описания логики фрагмента FragmentFilterArea
// Потом заменить на модель слоя domain

@Parcelize
data class AreaUi(
    val areaId: Int,
    val areaName: String,
    val parentId: Int? = null
) : Parcelable
