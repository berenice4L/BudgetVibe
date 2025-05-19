package com.example.budgetvibes.Data

import android.app.Application

class LoggedInUser() : Application(){

    var name:String = "name"
    override fun onCreate() {
        super.onCreate()
        name = ""
    }
}