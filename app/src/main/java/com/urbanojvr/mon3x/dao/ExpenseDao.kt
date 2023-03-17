package com.urbanojvr.mon3x.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.urbanojvr.mon3x.model.Expense

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM EXPENSE")
    fun getAll(): List<Expense>

    @Query("SELECT * FROM EXPENSE WHERE ID IN (:ids)")
    fun getAllByIds(expenseIds: IntArray): List<Expense>

    @Insert
    fun insertAll(vararg expenses: List<Expense>)

    @Delete
    fun delete(expense: Expense)

    @Delete
    fun deleteById(expenseId: Int)
}