package ru.practicum.android.diploma.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ContactsDto (
    val id: Int,
    val name: String,
    val email: String,
    val phones: List<PhoneDto>
) : Parcelable
