package ru.practicum.android.diploma.data.dto

class SomeResponse (val resultCount: Int,
                    val expression: String,
                    val results: List<SomeDto>) : Response()
