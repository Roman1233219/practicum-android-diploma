package ru.practicum.android.diploma.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SalaryDto (
    val from: Int?,
    val to: Int?,
    val currency: String?
) : Parcelable
