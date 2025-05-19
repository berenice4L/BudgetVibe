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
import com.example.budgetvibes.Data.Income
import com.example.budgetvibes.R

class IncomeAdapter(private var incomes: List<Income>,context: Context):
RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IncomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.income_item,parent,false)
        return IncomeViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: IncomeViewHolder,
        position: Int
    ) {
        val income = incomes[position]
        holder.receipt.setImageBitmap(byteArrayToBitmap(income.receiptPhoto))
        holder.categoryTextView.text = income.category
        holder.dateTextView.text = income.date
        holder.amountTextView.text = "R ${income.amount}"
    }

    override fun getItemCount(): Int {
       return incomes.size
    }

    class IncomeViewHolder(incomeItemView: View): RecyclerView.ViewHolder(incomeItemView) {
        val receipt :ImageView = incomeItemView.findViewById(R.id.receiptImageView)
        val categoryTextView:TextView = incomeItemView.findViewById(R.id.categoryTextView)
        val dateTextView:TextView =incomeItemView.findViewById(R.id.dateTextView)
        val amountTextView:TextView = incomeItemView.findViewById(R.id.amountTextView)
    }
    fun byteArrayToBitmap(imageBytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
    fun updateIncomeUi(newItems:List<Income>){
        incomes = newItems
        notifyDataSetChanged()
    }
}