package ru.practicum.android.diploma.domain.converters

import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.domain.models.Salary

fun SalaryDto.toModel(): Salary = Salary(this.from, this.to, this.currency)

fun Salary.toDto(): SalaryDto = SalaryDto(this.from, this.to, this.currency)
