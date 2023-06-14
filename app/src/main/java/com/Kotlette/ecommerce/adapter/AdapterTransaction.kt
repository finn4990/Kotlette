package com.Kotlette.ecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.item.ItemTransaction

class AdapterTransaction (private val transactionList : ArrayList<ItemTransaction>) :
    RecyclerView.Adapter<AdapterTransaction.ViewHolderTransaction>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTransaction {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolderTransaction(itemView)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: ViewHolderTransaction, position: Int) {
        val currentItem = transactionList[position]
        holder.code.text = "Data: " + currentItem.code
        holder.address.text = "Indirizzo: " + currentItem.address
        holder.date.text = "Data: " + currentItem.date
        holder.total.text = "Importo: " + currentItem.total
    }

    class ViewHolderTransaction(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val code : TextView = itemView.findViewById(R.id.transaction_code)
        val address : TextView = itemView.findViewById(R.id.address)
        val date : TextView = itemView.findViewById(R.id.date)
        val total : TextView = itemView.findViewById(R.id.total)
    }


}