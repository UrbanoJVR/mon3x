package com.urbanojvr.mon3x.dao

import androidx.room.Database
import com.urbanojvr.mon3x.model.Expense

@Database(entities = [Expense::class], version = 1)
abstract class AppDatabase {
    abstract fun expenseDao(): ExpenseDao;
}