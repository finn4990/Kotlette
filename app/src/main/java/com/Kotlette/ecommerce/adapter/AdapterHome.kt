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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHome {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ViewHolderHome(itemView)
    }

    override fun getItemCount(): Int {
        return homeList.size
    }

    override fun onBindViewHolder(holder: ViewHolderHome, position: Int) {
        val currentItem = homeList[position]
        holder.title.text = currentItem.title
        holder.image.setImageBitmap(currentItem.image)
        holder.price.text = "Prezzo: " + currentItem.price
    }

    class ViewHolderHome(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title : TextView = itemView.findViewById(R.id.title_product)
        val image : ImageView = itemView.findViewById(R.id.image_product)
        val price : TextView = itemView.findViewById(R.id.price_product)
    }
}