package com.example.budgetvibes.UI

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.budgetvibes.Data.Card
import com.example.budgetvibes.Databases.CardDB
import com.example.budgetvibes.R
import com.example.budgetvibes.databinding.ActivityAddCardBinding
import java.util.Calendar

class AddCardActivity : AppCompatActivity() {

    private lateinit var addCardBinding: ActivityAddCardBinding
    private lateinit var cardDB: CardDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        addCardBinding = ActivityAddCardBinding.inflate(layoutInflater)
        setContentView(addCardBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cardDB = CardDB(this)

        //calendar implementation for calendar picker button
        fun showDatePicker(context: Context, onDateSelected: (day: Int, month: Int, year: Int) -> Unit) {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    onDateSelected(selectedDay, selectedMonth + 1, selectedYear) // Month is 0-based
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        addCardBinding.dateButton.setOnClickListener {
            showDatePicker(this) { day, month, year ->
                val selectedDate = "${year.toString().subSequence(2,4)}/${month}"
                //setting the date after it is chosen
                addCardBinding.expiryDateText.text = selectedDate
            }
        }

        addCardBinding.createCardButton.setOnClickListener {
            val cardNumber = addCardBinding.cardNumberEditText.text.toString()

            val numDigits = cardNumber.length
            if (numDigits != 16){
                Toast.makeText(this,"Card Digits must be 16", Toast.LENGTH_SHORT).show()
            }else{

                val holder = addCardBinding.cardHolderEditText.text.toString()
                if(holder ==""){
                    Toast.makeText(this,"Card Holder Cannot be Empty", Toast.LENGTH_SHORT).show()
                }
                else{
                    val expiryDate = addCardBinding.expiryDateText.text.toString()
                    if(expiryDate == "01/01" || expiryDate ==""){
                        Toast.makeText(this,"Please specify date", Toast.LENGTH_SHORT).show()
                    }else{
                        val card = Card(0,cardNumber,holder,expiryDate)
                        cardDB.addNewCard(card)
                        Toast.makeText(this,"Card Create Successfully", Toast.LENGTH_SHORT).show()
                        val backIntent = Intent(this, CardsActivity::class.java)
                        startActivity(backIntent)
                        finish()
                    }

                }


            }




        }
        // back button
        addCardBinding.backButton.setOnClickListener {
            val backIntent = Intent(this, CardsActivity::class.java)
            startActivity(backIntent)
            finish()
        }
    }
}