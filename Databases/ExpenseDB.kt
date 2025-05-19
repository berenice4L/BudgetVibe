package com.example.budgetvibes.Databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.example.budgetvibes.Data.Expense

import kotlinx.coroutines.processNextEventInCurrentThread
import java.sql.Blob

class ExpenseDB(context: Context):SQLiteOpenHelper(context, DatabaseName,null, DatabaseVersion) {
    companion object{
        private const val DatabaseName = "ExpensesDb"
        private const val DatabaseVersion = 1
        private const val TableName = "Expenses"
        private const val ExpenseId = "ExpenseId"
        private const val Category = "Category"
        private const val Notes = "Notes"
        private const val Date = "Date"
        private const val Amount = "Amount"
        private const val ExpenseImage = "ExpenseImage"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createExpenseTable = "CREATE TABLE $TableName (" +
                "$ExpenseId INTEGER PRIMARY KEY," +
                "$Category TEXT," +
                "$Notes TEXT," +
                "$Date DATE," +
                "$Amount DOUBLE," +
                "$ExpenseImage BLOB)"
        db?.execSQL(createExpenseTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TableName"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun addNewExpense(expense:Expense){
        val expenseDatabase = writableDatabase
        val values = ContentValues().apply {
            put(Category,expense.category)
            put(Notes,expense.notes)
            put(Date,expense.date)
            put(Amount,expense.amount)
            put(ExpenseImage,expense.receiptPhoto)
        }
        expenseDatabase.insert(TableName,null,values)
    }

    fun getExpenses():List<Expense>{
        val expenseDb = readableDatabase
        val latestExpenses = mutableListOf<Expense>()
        val selectQuery = "SELECT * FROM $TableName"
        val cursor = expenseDb.rawQuery(selectQuery,null)

        while (cursor.moveToNext()){
            val expenseId = cursor.getInt(cursor.getColumnIndexOrThrow(ExpenseId))
            val category = cursor.getString(cursor.getColumnIndexOrThrow(Category))
            val notes = cursor.getString(cursor.getColumnIndexOrThrow(Notes))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(Date))
            val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(Amount))
            val receiptPhoto = cursor.getBlob(cursor.getColumnIndexOrThrow(ExpenseImage))

            val expenseCreated = Expense(expenseId,category,notes,date,amount,receiptPhoto)
            latestExpenses.add(expenseCreated)
        }
        cursor.close()
        expenseDb.close()
        return latestExpenses
    }

    fun getTotalExpenses():Double{
        val expenseDb = readableDatabase
        val query = "SELECT * FROM $TableName"
        val cursor = expenseDb.rawQuery(query,null)
        var total = 0.0
        while(cursor.moveToNext()){
            val amount  = cursor.getDouble(cursor.getColumnIndexOrThrow(Amount))
            total = total + amount
        }
        cursor.close()
        expenseDb.close()
        return total
    }


}