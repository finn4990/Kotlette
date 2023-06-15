package com.Kotlette.ecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.item.ItemCart

class AdapterCart (private val cartList : ArrayList<ItemCart>) :
    RecyclerView.Adapter<AdapterCart.ViewHolderCart>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCart {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolderCart(itemView)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: ViewHolderCart, position: Int) {
        val currentItem = cartList[position]
        holder.iconProduct.setImageResource(currentItem.iconProduct)
        holder.title.text = currentItem.title
    }

    class ViewHolderCart(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val iconProduct : ImageView = itemView.findViewById(R.id.image_product)
        val title : TextView = itemView.findViewById(R.id.title_product)
    }
}