package com.example.budgetvibes.UI

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.budgetvibes.Data.LoggedInUser
import com.example.budgetvibes.Databases.UserDB
import com.example.budgetvibes.R
import com.example.budgetvibes.databinding.ActivityLoginBinding
import com.example.budgetvibes.email
import com.example.budgetvibes.globalName

class LoginActivity : AppCompatActivity() {

   private lateinit var loginBinding:ActivityLoginBinding
   private lateinit var userDB: UserDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userDB = UserDB(this)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        loginBinding.loginButton.setOnClickListener {
            val username = loginBinding.usernameEmailEditText.text.toString()
            val password = loginBinding.passwordEditText.text.toString()

            if(userDB.checkUser(username,password)){

                //setting global variables to carry user details
                globalName = username

                //getting user details
                val user = userDB.getUserDetails(username,password)
                email = user.email

                Toast.makeText(this,"Log In Successful",Toast.LENGTH_SHORT).show()
                val homeIntent = Intent(this,MainActivity::class.java)
                startActivity(homeIntent)
                finish()
            }else{
                Toast.makeText(this,"Credentials are Invalid",Toast.LENGTH_SHORT).show()
            }
        }
        loginBinding.registerRedirectText.setOnClickListener {
            val registerIntent = Intent(this,RegisterActivity::class.java)
            startActivity(registerIntent)
        }
    }
}