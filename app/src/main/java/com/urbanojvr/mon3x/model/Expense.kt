package com.urbanojvr.mon3x.model

import java.time.LocalDate

data class Expense(
    val amount: String,
    val concept: String,
    val date: LocalDate
)
