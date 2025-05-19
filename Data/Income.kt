package com.example.budgetvibes.Data

data class Income(val incomeId:Int,val category:String,val notes:String,
                  val date:String,val amount:Double,val receiptPhoto:ByteArray)
