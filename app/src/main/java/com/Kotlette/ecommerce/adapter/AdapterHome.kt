package com.Kotlette.ecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.item.ItemHome

class AdapterHome (private val homeList : ArrayList<ItemHome>) :
    RecyclerView.Adapter<AdapterHome.ViewHolderHome>() {

    // Variabile per tenere traccia del listener per l'evento di clic sugli elementi della lista
    private lateinit var listener: OnItemClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHome {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ViewHolderHome(itemView, listener)
    }

    // Restituisce il numero totale di elementi nella homeList
    override fun getItemCount(): Int {
        return homeList.size
    }

    override fun onBindViewHolder(holder: ViewHolderHome, position: Int) {
        val currentItem = homeList[position]
        holder.title.text = currentItem.title
        holder.image.setImageBitmap(currentItem.image)
        holder.price.text = "Prezzo: " + currentItem.price
    }

    class ViewHolderHome(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val title : TextView = itemView.findViewById(R.id.title_product)
        val image : ImageView = itemView.findViewById(R.id.image_product_cart)
        val price : TextView = itemView.findViewById(R.id.price_product)


        init {
            // Imposta il listener per il click sull'elemento della lista
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }
}