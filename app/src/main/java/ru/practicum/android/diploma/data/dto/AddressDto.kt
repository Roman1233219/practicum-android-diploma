package ru.practicum.android.diploma.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressDto (
    val id: Int,
    val city: String,
    val street: String,
    val building: String,
    val raw: String
) : Parcelable
