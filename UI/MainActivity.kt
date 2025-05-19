package com.example.budgetvibes.UI

import android.content.Intent
import android.os.Bundle
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetvibes.Adapters.CardAdapter
import com.example.budgetvibes.Adapters.ExpensesAdapter
import com.example.budgetvibes.Adapters.IncomeAdapter
import com.example.budgetvibes.Data.LoggedInUser
import com.example.budgetvibes.Databases.CardDB
import com.example.budgetvibes.Databases.ExpenseDB
import com.example.budgetvibes.Databases.IncomeDB
import com.example.budgetvibes.R
import com.example.budgetvibes.databinding.ActivityLoginBinding
import com.example.budgetvibes.databinding.ActivityMainBinding
import com.example.budgetvibes.globalName
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.exp

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityLoginBinding: ActivityMainBinding
    private lateinit var bottomNav : BottomNavigationView
    private lateinit var expenseDB: ExpenseDB
    private lateinit var incomeDB: IncomeDB
    private lateinit var expensesAdapter: ExpensesAdapter
    private lateinit var cardDB: CardDB
    private lateinit var cardAdapter: CardAdapter
    private lateinit var incomeAdapter: IncomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainActivityLoginBinding = ActivityMainBinding.inflate(layoutInflater)

        expenseDB = ExpenseDB(this)
        incomeDB = IncomeDB(this)
        cardDB = CardDB(this)

        setContentView(mainActivityLoginBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        expenseDB = ExpenseDB(this)
        expensesAdapter = ExpensesAdapter(expenseDB.getExpenses(),this)
        mainActivityLoginBinding.transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        mainActivityLoginBinding.transactionsRecyclerView.adapter = expensesAdapter

        val logUser = LoggedInUser()


        //implementing income adapter
        incomeAdapter = IncomeAdapter(incomeDB.getIncomeTransactions(),this)

        //implementing the recycler view for the cards
        cardAdapter = CardAdapter(cardDB.getCards(),this)

        //setting up the recycler layout manager and adapter

        mainActivityLoginBinding.cardsRecyclerView.layoutManager = LinearLayoutManager(this)
        mainActivityLoginBinding.cardsRecyclerView.adapter = cardAdapter

        mainActivityLoginBinding.expenseChip.setOnClickListener {
            val addExpenseIntent = Intent(this,PaymentActivity::class.java)
            startActivity(addExpenseIntent)
        }
        mainActivityLoginBinding.incomeChip.setOnClickListener {
            val addIncomeIntent = Intent(this, IncomeActivity::class.java)
            startActivity(addIncomeIntent)
        }

        // balance logic

        val totalExpenses = expenseDB.getTotalExpenses()
        val totalIncome = incomeDB.totalIncome()
        val balance = totalIncome - totalExpenses

        mainActivityLoginBinding.totalIncomeText.text = "${formatAsCurrency(totalIncome)}"
        mainActivityLoginBinding.totalExpensesText.text = "${formatAsCurrency(totalExpenses)}"

        mainActivityLoginBinding.balanceAmountText.text = "${formatAsCurrency(balance)}"

        // initialising the bottom nav
        bottomNav = mainActivityLoginBinding.bottomNavigationView

        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> true
                R.id.nav_history ->{
                    startActivity(Intent(this,HistoryActivity::class.java))
                    overridePendingTransition(0,0)
                    true

                }
                R.id.nav_cards->{
                    startActivity(Intent(this, CardsActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.nav_profile->{
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                else -> false
            }

        }

    }
    //double to currency
    fun formatAsCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(amount)
    }
    override fun onResume() {
        super.onResume()
        expensesAdapter.updateUi(expenseDB.getExpenses())
        incomeAdapter.updateIncomeUi(incomeDB.getIncomeTransactions())

    }

}