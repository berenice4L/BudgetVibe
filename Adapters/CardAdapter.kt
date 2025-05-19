package com.example.budgetvibes.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetvibes.Data.Card
import com.example.budgetvibes.R

class CardAdapter(private var cards: List<Card>,context: Context):
RecyclerView.Adapter<CardAdapter.CardViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item,parent,false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CardViewHolder,
        position: Int
    ) {
        val card = cards[position]
        val cardDigits = card.digits.toString().trim()
        val firstGroup = cardDigits.subSequence(0,4)
        val secondGroup = cardDigits.subSequence(4,8)
        val thirdGroup = cardDigits.subSequence(8,12)
        val fourthGroup = cardDigits.subSequence(12,16)

        holder.cardHolder.text = card.cardHolder
        holder.digits.text = "$firstGroup $secondGroup $thirdGroup $fourthGroup"
        holder.expiryDate.text = card.expiryDate



    }

    override fun getItemCount(): Int {
        return cards.size
    }

    class CardViewHolder(cardItemView: View): RecyclerView.ViewHolder(cardItemView) {
        val digits: TextView = cardItemView.findViewById(R.id.cardNumberText)
        val cardHolder: TextView = cardItemView.findViewById(R.id.cardHolderText)
        val expiryDate: TextView = cardItemView.findViewById(R.id.expiryText)
    }
}