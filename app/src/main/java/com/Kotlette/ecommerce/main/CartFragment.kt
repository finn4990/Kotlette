package com.Kotlette.ecommerce.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.R
import com.Kotlette.ecommerce.adapter.AdapterCart
import com.Kotlette.ecommerce.databinding.FragmentCartBinding
import com.Kotlette.ecommerce.item.ItemCart
import com.Kotlette.ecommerce.item.SingletonCart

class CartFragment : Fragment() {

    private lateinit var adapter : AdapterCart
    private lateinit var recyclerView : RecyclerView
    private lateinit var cartArrayList : ArrayList<ItemCart>

    lateinit var title: Array<String>
    lateinit var iconProduct: Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecyclerViewCart
        recyclerView = view.findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        adapter = AdapterCart(SingletonCart.getCart())
        recyclerView.adapter = adapter
    }
}