package com.urbanojvr.mon3x.service

import android.util.Log
import com.urbanojvr.mon3x.model.Expense

class ExpenseService {

    fun saveExpense(expense: Expense) {
        Log.d("Mon3x", expense.toString())
    }
}