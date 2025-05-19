package com.example.budgetvibes.UI

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.budgetvibes.Adapters.ExpensesAdapter
import com.example.budgetvibes.Adapters.IncomeAdapter
import com.example.budgetvibes.Data.Income
import com.example.budgetvibes.Databases.ExpenseDB
import com.example.budgetvibes.Databases.IncomeDB
import com.example.budgetvibes.R
import com.example.budgetvibes.databinding.ActivityHistoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyBinding:ActivityHistoryBinding
    private lateinit var expenseDB: ExpenseDB
    private lateinit var incomeDB: IncomeDB
    private lateinit var expensesAdapter: ExpensesAdapter
    private lateinit var bottomNav : BottomNavigationView
    private lateinit var incomeAdapter: IncomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        historyBinding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(historyBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        historyBinding.incomeButton.setOnClickListener {
            val incomeIntent = Intent(this, IncomeHistoryActivity::class.java)
            startActivity(incomeIntent)
        }
        //bottom navigation logic
        bottomNav  = historyBinding.bottomNavigationView
        bottomNav.selectedItemId = R.id.nav_history

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.nav_history ->{
                    true
                }
                R.id.nav_cards ->{
                    startActivity(Intent(this, CardsActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.nav_profile ->{
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                else -> false
            }

        }
        historyBinding.addExpenseFloatingActionButton.setOnClickListener {
            val addIntent = Intent(this, PaymentActivity::class.java)
            startActivity(addIntent)

        }



        expenseDB = ExpenseDB(this)
        expensesAdapter = ExpensesAdapter(expenseDB.getExpenses(),this)

        incomeDB = IncomeDB(this)
        incomeAdapter = IncomeAdapter(incomeDB.getIncomeTransactions(),this)



        historyBinding.expensesRecyclerView.layoutManager = LinearLayoutManager(this)
        historyBinding.expensesRecyclerView.adapter = expensesAdapter

        //binding the total amount of expenses to the view
        val totalAmountExpenses = expenseDB.getTotalExpenses()
        historyBinding.balanceAmountText.text = "R $totalAmountExpenses"
    }

    override fun onResume() {
        super.onResume()
        expensesAdapter.updateUi(expenseDB.getExpenses())
    }
}