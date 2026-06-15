package ru.practicum.android.diploma.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VacancyDetailDto (
    val id: Int,
    val nam: String,
    val description: String,
    val salary: Salary,
    val address: AddressDto?,
    val experience: ExperienceDto?,
    val schedule: ScheduleDto?,
    val employment: EmploymentDto?,
    val contacts: ContactsDto?,
    val employer: Employer,
    val area: FilterArea,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustry
) : Parcelable
