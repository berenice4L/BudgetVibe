package com.example.budgetvibes.Databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.budgetvibes.Data.User

class UserDB(context: Context):SQLiteOpenHelper(context, DatabaseName,null, DatabaseVersion) {
    companion object{
        private const val DatabaseName = "UsersDatabase"
        private const val DatabaseVersion = 3
        private const val TableName = "Users"
        private const val UserId = "UserId"
        private const val Username = "Username"
        private const val Email = "Email"
        private const val Password = "Password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTableQuery = "CREATE TABLE $TableName(" +
                "$UserId INTEGER PRIMARY KEY," +
                "$Username TEXT," +
                "$Password TEXT," +
                "$Email TEXT)"
        db?.execSQL(createUserTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TableName"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun addNewUser(user:User){
        val userDatabase = writableDatabase
        val values = ContentValues().apply {
            put(UserId,user.userId)
            put(Username,user.username)
            put(Email,user.email)
            put(Password,user.password)

        }
        userDatabase.insert(TableName,null,values)
    }
    fun checkUser(username:String,password:String):Boolean{
        val userDatabase = readableDatabase

        val checkColumns = "$Username = ? AND $Password = ?"
        val searchTerms = arrayOf(username,password)
        val cursor = userDatabase.query(TableName,null,checkColumns,searchTerms,null,null,null)

        val checkUser = cursor.count > 0
        return checkUser
    }
    fun getUserDetails(username:String,password:String): User{
        val userDatabase = readableDatabase

        val checkColumns = "$Username = ? AND $Password = ?"
        val searchTerms = arrayOf(username,password)
        val cursor = userDatabase.query(TableName,null,checkColumns,searchTerms,null,null,null)

        var user = User(0,"","","")
        while (cursor.moveToNext()){
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow(UserId))
            val username = cursor.getString(cursor.getColumnIndexOrThrow(Username))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(Email))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(Password))
            user = User(userId,username,email,password)
        }
        return user
    }
}