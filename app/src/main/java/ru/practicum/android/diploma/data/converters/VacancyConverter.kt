package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.Vacancy

fun VacanciesResponse.toModel(): VacanciesSearchResult = VacanciesSearchResult(
    vacancies = this.items.map { it.toModel() },
    vacanciesFound = this.found,
    pagesCount = this.pages,
    currentPage = this.page
)

fun VacancyDto.toModel(): Vacancy = Vacancy(
    vacancyId = this.id,
    vacancyName = this.name,
    employerId = this.employer?.id,
    companyName = this.employer?.name,
    areaId = this.area?.id,
    areaName = this.area?.name,
    salaryFrom = this.salary?.from,
    salaryTo = this.salary?.to,
    currency = this.salary?.currency,
    logoUrl = this.employer?.logoUrls?.original,
    description = this.description,
    experienceName = this.experience?.name,
    scheduleName = this.schedule?.name,
    employmentName = this.employment?.name,
    addressRaw = this.address?.formatted,
    skills = this.keySkills?.mapNotNull { it.name },
    contactName = this.contacts?.name,
    contactEmail = this.contacts?.email,
    phoneFormatted = this.contacts?.phones?.firstOrNull()?.formatted,
    shareUrl = this.alternateUrl
)

fun Vacancy.toDto(): VacancyDto = VacancyDto(
    id = this.vacancyId,
    name = this.vacancyName,
    salary = SalaryDto(
        from = this.salaryFrom,
        to = this.salaryTo,
        currency = this.currency
    ),
    employer = ru.practicum.android.diploma.data.dto.EmployerDto(
        id = this.employerId,
        name = this.companyName,
        logoUrls = ru.practicum.android.diploma.data.dto.LogoUrlsDto(
            original = this.logoUrl
        )
    ),
    area = ru.practicum.android.diploma.data.dto.AreaDto(
        id = this.areaId,
        name = this.areaName
    ),
    description = this.description,
    keySkills = this.skills?.map { ru.practicum.android.diploma.data.dto.KeySkillDto(it) },
    experience = ru.practicum.android.diploma.data.dto.ExperienceDto(this.experienceName),
    schedule = ru.practicum.android.diploma.data.dto.ScheduleDto(this.scheduleName),
    employment = ru.practicum.android.diploma.data.dto.EmploymentDto(this.employmentName),
    address = ru.practicum.android.diploma.data.dto.AddressDto(this.addressRaw),
    contacts = ru.practicum.android.diploma.data.dto.ContactsDto(
        name = this.contactName,
        email = this.contactEmail,
        phones = if (this.phoneFormatted != null) listOf(ru.practicum.android.diploma.data.dto.PhoneDto(this.phoneFormatted)) else null
    ),
    alternateUrl = this.shareUrl
)
