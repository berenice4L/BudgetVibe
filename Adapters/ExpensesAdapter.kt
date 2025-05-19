package com.example.budgetvibes.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetvibes.Data.Expense
import com.example.budgetvibes.R
import org.w3c.dom.Text
import kotlin.math.exp

class ExpensesAdapter(private var expenses:List<Expense>,context:Context):
    RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder>() {

    class ExpensesViewHolder(expenseItemView:View):RecyclerView.ViewHolder(expenseItemView) {
        val receipt :ImageView = expenseItemView.findViewById(R.id.receiptImageView)
        val categoryTextView:TextView = expenseItemView.findViewById(R.id.categoryTextView)
        val dateTextView:TextView =expenseItemView.findViewById(R.id.dateTextView)
        val amountTextView:TextView = expenseItemView.findViewById(R.id.amountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val expenseView = LayoutInflater.from(parent.context).inflate(R.layout.expense_item,parent,false)
        return ExpensesViewHolder(expenseView)
    }

    override fun getItemCount() = expenses.size

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        val expense = expenses[position]
        holder.receipt.setImageBitmap(byteArrayToBitmap(expense.receiptPhoto))
        holder.categoryTextView.text = expense.category
        holder.dateTextView.text = expense.date
        holder.amountTextView.text = "R ${expense.amount}"
    }
    fun byteArrayToBitmap(imageBytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
    fun updateUi(newItems:List<Expense>){
        expenses = newItems
        notifyDataSetChanged()
    }

}