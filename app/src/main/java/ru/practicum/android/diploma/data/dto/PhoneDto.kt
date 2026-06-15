package ru.practicum.android.diploma.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhoneDto (
    val comment: String?,
    val formatted: String
) : Parcelable
