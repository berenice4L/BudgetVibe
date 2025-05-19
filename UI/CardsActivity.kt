package com.example.budgetvibes.UI

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetvibes.Adapters.CardAdapter
import com.example.budgetvibes.Databases.CardDB
import com.example.budgetvibes.R
import com.example.budgetvibes.databinding.ActivityCardsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CardsActivity : AppCompatActivity() {

    private lateinit var cardsBinding: ActivityCardsBinding
    private lateinit var cardsDB: CardDB
    private lateinit var cardsAdapter: CardAdapter
    private lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        cardsBinding = ActivityCardsBinding.inflate(layoutInflater)
        setContentView(cardsBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //implementing the database and adapter
        cardsDB = CardDB(this)
        cardsAdapter = CardAdapter(cardsDB.getCards(),this)

        // implementing the recycler view
        cardsBinding.cardsRecyclerView.layoutManager = LinearLayoutManager(this)
        cardsBinding.cardsRecyclerView.adapter = cardsAdapter

        cardsBinding.addCardButton.setOnClickListener {
            val addCardIntent= Intent(this, AddCardActivity::class.java)
            startActivity(addCardIntent)
            finish()
        }
        bottomNav = cardsBinding.bottomNavigationView

        bottomNav.selectedItemId = R.id.nav_cards

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.nav_history ->{
                    startActivity(Intent(this,HistoryActivity::class.java))
                    overridePendingTransition(0,0)
                    true

                }
                R.id.nav_cards->{
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
}