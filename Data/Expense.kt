package com.example.budgetvibes.Data

import java.sql.Blob

data class Expense(val expenseId:Int,val category:String,val notes:String,
                   val date:String,val amount:Double,val receiptPhoto:ByteArray)
