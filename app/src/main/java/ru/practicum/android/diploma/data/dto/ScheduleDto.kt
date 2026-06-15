package ru.practicum.android.diploma.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleDto (
    val id: Int,
    val name: String
) : Parcelable
