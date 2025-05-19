package com.example.budgetvibes.UI

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetvibes.Adapters.IncomeAdapter
import com.example.budgetvibes.Databases.IncomeDB
import com.example.budgetvibes.R
import com.example.budgetvibes.databinding.ActivityIncomeHistoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class IncomeHistoryActivity : AppCompatActivity() {

    private lateinit var incomeHistoryBinding: ActivityIncomeHistoryBinding
    private lateinit var incomeDB: IncomeDB
    private lateinit var incomeAdapter: IncomeAdapter
    private lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        incomeHistoryBinding = ActivityIncomeHistoryBinding.inflate(layoutInflater)
        incomeDB = IncomeDB(this)
        setContentView(incomeHistoryBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //bottom navigation
        bottomNav  = incomeHistoryBinding.bottomNavigationView
        bottomNav.selectedItemId = R.id.nav_history

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(2,0)
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

        // total income amount
        val totalIncome = incomeDB.totalIncome()
        incomeHistoryBinding.balanceIncomeAmountText.text = totalIncome.toString()

        incomeHistoryBinding.expensesButton.setOnClickListener {
            val expAdapter = Intent(this, HistoryActivity::class.java)
            startActivity(expAdapter)
        }

        incomeHistoryBinding.addIncomeFloatingActionButton.setOnClickListener {
            val addIntent = Intent(this, IncomeActivity::class.java)
            startActivity(addIntent)

        }


        incomeAdapter = IncomeAdapter(incomeDB.getIncomeTransactions(),this)

        incomeHistoryBinding.incomeRecyclerView.layoutManager = LinearLayoutManager(this)
        incomeHistoryBinding.incomeRecyclerView.adapter = incomeAdapter
    }

    override fun onResume() {
        super.onResume()
        incomeAdapter.updateIncomeUi(incomeDB.getIncomeTransactions())
    }
}