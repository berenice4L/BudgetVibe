package com.example.budgetvibes.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.budgetvibes.Databases.ExpenseDB
import com.example.budgetvibes.Databases.IncomeDB
import com.example.budgetvibes.R
import com.example.budgetvibes.databinding.ActivityProfileBinding
import com.example.budgetvibes.email
import com.example.budgetvibes.globalName
import kotlin.math.exp
import kotlin.math.log

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileBinding: ActivityProfileBinding
    private lateinit var expenseDB: ExpenseDB
    private lateinit var incomeDB: IncomeDB
    private lateinit var profilePhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //initialising the database classes
        expenseDB = ExpenseDB(this)
        incomeDB = IncomeDB(this)

        val totalExpenses = expenseDB.getExpenses().size.toString()
        val totalIncome = incomeDB.getIncomeTransactions().size.toString()

        profileBinding.numberOfExpensesText.text = totalExpenses
        profileBinding.numberOfIncomeText.text = totalIncome

        // initialising the profile photo image view
        profilePhoto = profileBinding.profileImage


        profileBinding.editProfileButton.setOnClickListener {
            getImage.launch("image/*")
        }


        //using global variables
        profileBinding.userNameText.text = globalName
        profileBinding.userEmailText.text = email

        profileBinding.logoutButton.setOnClickListener {
            val logOutIntent = Intent(this, LoginActivity::class.java)
            startActivity(logOutIntent)
            finish()
        }
        profileBinding.backButton.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
            finish()
        }
    }
    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            profilePhoto.setImageURI(it)
        }
    }
}