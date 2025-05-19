package com.example.budgetvibes.Databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.budgetvibes.Data.Card

class CardDB(context: Context): SQLiteOpenHelper(context,Database,null,Version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableCards = "CREATE TABLE $TableName(" +
                "$CardId INTEGER PRIMARY KEY," +
                "$CardHolder TEXT," +
                "$Digits TEXT," +
                "$ExpiryDate TEXT)"
        db?.execSQL(createTableCards)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        val dropTable = "DROP TABLE IF EXISTS $TableName"
        db?.execSQL(dropTable)
        onCreate(db)
    }
    fun addNewCard(card: Card){
        val cardDb = writableDatabase
        val values = ContentValues().apply {
            put(Digits,card.digits)
            put(CardHolder,card.cardHolder)
            put(ExpiryDate,card.expiryDate)
        }
        cardDb.insert(TableName,null,values)
    }

    fun getCards():List<Card>{
        val cardDb = readableDatabase
        val selectQuery = "SELECT * FROM $TableName"
        val cursor = cardDb.rawQuery(selectQuery,null)
        var cards = mutableListOf<Card>()

        while (cursor.moveToNext()){
            val cardId = cursor.getInt(cursor.getColumnIndexOrThrow(CardId))
            val cardHolder = cursor.getString(cursor.getColumnIndexOrThrow(CardHolder))
            val expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(ExpiryDate))
            val digits = cursor.getString(cursor.getColumnIndexOrThrow(Digits))

            val card = Card(cardId,digits,cardHolder,expiryDate)
            cards.add(card)
        }
        cursor.close()
        cardDb.close()
        return cards
    }

    companion object{
        private const val Database = "CardsDb"
        private const val Version = 1
        private const val TableName = "CardsTable"
        private const val CardId = "CardId"
        private const val CardHolder = "CardHolder"
        private const val ExpiryDate = "Date"
        private const val Digits = "Digits"
    }
}