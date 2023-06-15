package com.Kotlette.ecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.item.ItemDetail

class AdapterDetail (private val detailList : ArrayList<ItemDetail>) :
    RecyclerView.Adapter<AdapterDetail.ViewHolderDetail>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDetail {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
        return ViewHolderDetail(itemView)
    }

    override fun getItemCount(): Int {
        return detailList.size
    }

    override fun onBindViewHolder(holder: ViewHolderDetail, position: Int) {
        val currentItem = detailList[position]
        holder.userIcon.setImageResource(currentItem.userIcon)
        holder.comment.text = currentItem.comment
        holder.rating.text = "VOTO: " + currentItem.rating
    }

    class ViewHolderDetail(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userIcon : ImageView = itemView.findViewById(R.id.user_icon)
        val comment : TextView = itemView.findViewById(R.id.comment)
        val rating : TextView = itemView.findViewById(R.id.rating)
    }
}