package com.urbanojvr.mon3x.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Expense(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "AMOUNT") val amount: String,
    @ColumnInfo(name = "CONCEPT") val concept: String,
    @ColumnInfo(name = "DATE") val date: LocalDate
)
