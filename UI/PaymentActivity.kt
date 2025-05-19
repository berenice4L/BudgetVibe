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
import com.example.budgetvibes.Data.Expense
import com.example.budgetvibes.Databases.ExpenseDB
import com.example.budgetvibes.R
import com.example.budgetvibes.databinding.ActivityPaymentBinding
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import java.util.Calendar
import kotlin.math.exp

class PaymentActivity : AppCompatActivity() {
    private lateinit var paymentActivityPaymentBinding: ActivityPaymentBinding
    private lateinit var expenseDB: ExpenseDB
    private lateinit var categorySpinner: Spinner
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        paymentActivityPaymentBinding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(paymentActivityPaymentBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Initialising the database
        expenseDB = ExpenseDB(this)
        //initialising spinner
        categorySpinner = paymentActivityPaymentBinding.categorySpinner

        // initialising category array
        val categories = mutableListOf("\uD83D\uDD0C Electricity","\uD83D\uDC55 Clothes")

        //implementation of spinner
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,categories)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = arrayAdapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                paymentActivityPaymentBinding.categoryChosen.text = selectedItem
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

        paymentActivityPaymentBinding.calenderImageView.setOnClickListener {
            showDatePicker(this) { day, month, year ->
                val selectedDate = "$year/$month/$day"
                //setting the date after it is chosen
                paymentActivityPaymentBinding.dateTextView.text = selectedDate
            }
        }

        //add photo button
        paymentActivityPaymentBinding.addPhotoButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
            }

        }

        // save expense button
        paymentActivityPaymentBinding.saveExpenseButton.setOnClickListener {
            // retrieving all expense details
            val id = 0
            val category = paymentActivityPaymentBinding.categoryChosen.text.toString().trim()
            val amount = paymentActivityPaymentBinding.expenseAmountTextView.text.toString().trim()
            val date = paymentActivityPaymentBinding.dateTextView.text.toString().trim()
            val description = paymentActivityPaymentBinding.descriptionTextView.text.toString()
            val expensePhoto = paymentActivityPaymentBinding.photoImageView

            val expenseLogged = Expense(id,category,description,date,amount.toDouble(),imageViewToByteArray(expensePhoto))
            expenseDB.addNewExpense(expenseLogged)
            Toast.makeText(this,"Transaction Added",Toast.LENGTH_LONG).show()
            val historyIntent = Intent(this, HistoryActivity::class.java)
            startActivity(historyIntent)
            finish()
        }

    }
    //function to launch camera to take photo
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            paymentActivityPaymentBinding.photoImageView.setImageBitmap(imageBitmap)

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