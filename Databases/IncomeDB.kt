package com.example.budgetvibes.Databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.budgetvibes.Data.Expense
import com.example.budgetvibes.Data.Income


class IncomeDB(context: Context): SQLiteOpenHelper(context,DatabaseName,null,DatabaseVersion) {


    companion object{
        private const val DatabaseName = "IncomeDb"
        private const val DatabaseVersion = 1
        private const val TableName = "IncomeTable"
        private const val IncomeId = "ExpenseId"
        private const val Category = "Category"
        private const val Notes = "Notes"
        private const val Date = "Date"
        private const val Amount = "Amount"
        private const val IncomeImage = "IncomeImage"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createIncomeTable = "CREATE TABLE $TableName (" +
                "$IncomeId INTEGER PRIMARY KEY," +
                "$Category TEXT," +
                "$Notes TEXT," +
                "$Date DATE," +
                "$Amount DOUBLE," +
                "$IncomeImage BLOB)"
        db?.execSQL(createIncomeTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TableName"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }
    fun addNewIncome(income: Income){
        val incomeDatabase = writableDatabase
        val values = ContentValues().apply {
            put(Category,income.category)
            put(Notes,income.notes)
            put(Date,income.date)
            put(Amount,income.amount)
            put(IncomeImage,income.receiptPhoto)
        }
        incomeDatabase.insert(TableName,null,values)
    }
    fun getIncomeTransactions():List<Income>{
        val incomeDb = readableDatabase
        val latestIncome = mutableListOf<Income>()
        val selectQuery = "SELECT * FROM $TableName"
        val cursor = incomeDb.rawQuery(selectQuery,null)

        while (cursor.moveToNext()){
            val incomeId = cursor.getInt(cursor.getColumnIndexOrThrow(IncomeId))
            val category = cursor.getString(cursor.getColumnIndexOrThrow(Category))
            val notes = cursor.getString(cursor.getColumnIndexOrThrow(Notes))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(Date))
            val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(Amount))
            val receiptPhoto = cursor.getBlob(cursor.getColumnIndexOrThrow(IncomeImage))

            val incomeCreated = Income(incomeId,category,notes,date,amount,receiptPhoto)
            latestIncome.add(incomeCreated)
        }
        cursor.close()
        incomeDb.close()
        return latestIncome
    }
    fun totalIncome():Double{
        val incomeDb = readableDatabase
        val query = "SELECT * FROM $TableName"
        val cursor = incomeDb.rawQuery(query,null)
        var total = 0.0
        while(cursor.moveToNext()){
            val amount  = cursor.getDouble(cursor.getColumnIndexOrThrow(Amount))
            total = total + amount
        }
        cursor.close()
        incomeDb.close()
        return total
    }

}