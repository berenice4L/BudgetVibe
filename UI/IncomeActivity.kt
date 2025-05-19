package com.example.budgetvibes.UI

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.budgetvibes.Data.Income
import com.example.budgetvibes.Databases.IncomeDB
import com.example.budgetvibes.R
import com.example.budgetvibes.UI.PaymentActivity.Companion.REQUEST_IMAGE_CAPTURE
import com.example.budgetvibes.databinding.ActivityIncomeBinding
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import java.util.Calendar

class IncomeActivity : AppCompatActivity() {

    private lateinit var incomeBinding: ActivityIncomeBinding
    private lateinit var incomeDB: IncomeDB
    private lateinit var incomeCategorySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        incomeBinding = ActivityIncomeBinding.inflate(layoutInflater)
        setContentView(incomeBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //initialising the income database for CRUD queries
        incomeDB = IncomeDB(this)


        // implementing the category spinner

        incomeCategorySpinner = incomeBinding.incomeCategorySpinner
        //list of income categories
        val categories = mutableListOf<String>("\uD83D\uDCB0 Salary",
            "\uD83D\uDC8E Rewards",
            "\uD83D\uDCB9 Investment",
            "\uD83D\uDCB5 Allowance",
            "\uD83D\uDCB3 Business",
            "\uD83E\uDE99 Other")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,categories)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        incomeCategorySpinner.adapter = arrayAdapter

        incomeCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                incomeBinding.categoryChosen.text = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // date picker


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

        incomeBinding.incomeCalenderImageView.setOnClickListener {
            showDatePicker(this) { day, month, year ->
                val selectedDate = "$year/$month/$day"
                //setting the date after it is chosen
                incomeBinding.incomeDateTextView.text = selectedDate
            }
        }

        //add photo button
        incomeBinding.addIncomePhotoButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
            }

        }

        incomeBinding.saveIncomeButton.setOnClickListener {
            // retrieving all expense details
            val id = 0
            val category = incomeBinding.categoryChosen.text.toString().trim()
            val amount = incomeBinding.incomeAmountTextView.text.toString().trim()
            val date = incomeBinding.incomeDateTextView.text.toString().trim()
            val description = incomeBinding.incomeDescriptionTextView.text.toString()
            val expensePhoto = incomeBinding.incomePhotoImageView

            val incomeLogged = Income(id,category,description,date,amount.toDouble(),imageViewToByteArray(expensePhoto))
            incomeDB.addNewIncome(incomeLogged)
            Toast.makeText(this,"Transaction Added",Toast.LENGTH_LONG).show()
            val historyIntent = Intent(this, IncomeHistoryActivity::class.java)
            startActivity(historyIntent)
            finish()
        }

        //bottom navigation


    }
    //function to launch camera to take photo
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            incomeBinding.incomePhotoImageView.setImageBitmap(imageBitmap)

        }
    }
    //function to convert an imageview into a byte array
    fun imageViewToByteArray(imageView: ImageView): ByteArray {
        // Get bitmap from the drawable of the ImageView
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        // Convert bitmap to byte array
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}