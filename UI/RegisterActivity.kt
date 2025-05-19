package com.example.budgetvibes.UI

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.budgetvibes.Data.User
import com.example.budgetvibes.Databases.UserDB
import com.example.budgetvibes.R
import com.example.budgetvibes.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewBinding:ActivityRegisterBinding
    private lateinit var userDB: UserDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // initializing view binding
        registerViewBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerViewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userDB = UserDB(this)

        registerViewBinding.registerButton.setOnClickListener {
            val username = registerViewBinding.usernameEditText.text.toString()
            val email = registerViewBinding.emailEditText.text.toString()
            val password = registerViewBinding.passwordEditText.text.toString()
            val confirmPassword = registerViewBinding.confirmPasswordEditText.text.toString()

            if(username != "" && email != ""){
                if(password == confirmPassword){
                    val user = User(0,username,email,password)
                    userDB.addNewUser(user)
                    Toast.makeText(this,"Registration Successful",Toast.LENGTH_SHORT).show()
                    val logInIntent = Intent(this,LoginActivity::class.java)
                    startActivity(logInIntent)
                    finish()
                }
                else{
                    Toast.makeText(this,"The passwords do not match",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Username and Email Cannot be Empty",Toast.LENGTH_SHORT).show()
            }
        }
    }
}