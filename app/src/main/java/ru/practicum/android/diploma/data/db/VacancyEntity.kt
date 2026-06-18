package ru.practicum.android.diploma.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "fav_vacancies_table")
@TypeConverters(StringListConverter::class)
data class VacancyEntity(
    @PrimaryKey
    @ColumnInfo(name = "vacancy_id")
    val vacancyId: String,
    val vacancyName: String,
    val employerId: String?,
    @ColumnInfo(name = "company_name")
    val companyName: String?,
    val areaId: String?,
    val areaName: String?,
    @ColumnInfo(name = "salary_from")
    val salaryFrom: Long?,
    val salaryTo: Long?,
    val currency: String?,
    val logoUrl: String?,
    val description: String?,
    val experienceName: String?,
    val scheduleName: String?,
    val employmentName: String?,
    val addressRaw: String?,
    @TypeConverters(StringListConverter::class)
    val skills: List<String>?,
    val contactName: String?,
    val contactEmail: String?,
    val phoneFormatted: String?,
    val shareUrl: String?
)
