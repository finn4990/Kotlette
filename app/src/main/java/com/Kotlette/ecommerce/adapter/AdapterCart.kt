package com.Kotlette.ecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.item.ItemCart
import com.Kotlette.ecommerce.item.SingletonCart

class AdapterCart (private val cartList : ArrayList<ItemCart>) :
    RecyclerView.Adapter<AdapterCart.ViewHolderCart>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCart {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolderCart(itemView)
    }

    // Restituisce il numero totale di elementi nella lista del carrello
    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: ViewHolderCart, position: Int) {
        val currentItem = cartList[position]
        holder.iconProduct.setImageBitmap(currentItem.image)
        holder.title.text = currentItem.title
        holder.price.text = "Price: " + currentItem.price.toString()
        holder.quantity.text = "Quantity: " + currentItem.qty

        // Gestisce l'azione del pulsante "Aggiungi quantità"
        holder.addQuantity.setOnClickListener{
            SingletonCart.addQuantity(position)
            notifyDataSetChanged()
        }

        // Gestisce l'azione del pulsante "Rimuovi quantità"
        holder.removeQuantity.setOnClickListener{
            SingletonCart.removeQuantity(position)
            notifyDataSetChanged()
        }

        // Gestisce l'azione del pulsante "Rimuovi prodotto"
        holder.removeProduct.setOnClickListener{
            SingletonCart.remFromCart(position)
            notifyDataSetChanged()
        }
    }

    class ViewHolderCart(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val iconProduct : ImageView = itemView.findViewById(R.id.image_product_cart)
        val title : TextView = itemView.findViewById(R.id.title_product)
        val price : TextView = itemView.findViewById(R.id.price_product_cart)
        val quantity : TextView = itemView.findViewById(R.id.quantity_cart)
        val addQuantity : Button = itemView.findViewById(R.id.add_quantity)
        val removeQuantity : Button = itemView.findViewById(R.id.remove_quantity)
        val removeProduct : Button = itemView.findViewById(R.id.remove_product)
    }
}