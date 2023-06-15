package com.Kotlette.ecommerce

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.adapter.AdapterCart
import com.Kotlette.ecommerce.item.ItemCart

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
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()

        //RecycleViewPopular
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        adapter = AdapterCart(cartArrayList)
        recyclerView.adapter = adapter


    }

    private fun dataInitialize() {

        cartArrayList = arrayListOf<ItemCart>()

        title = arrayOf(
            "Via Badia 9",
            "Via Tiepolo 15",
            "Piazza Navona 45A",
            "Srada Longevo 8",
            "Viale Regione Siciliana 77",
            "Via Cordova 6",
            "Via M. De Cervantes 2",
            "Piazza A. Arrigo 4",
            "Via Badia 9",
            "Via Tiepolo 15",
            "Piazza Navona 45A",
            "Srada Longevo 8",
            "Viale Regione Siciliana 77",
            "Via Cordova 6",
            "Via M. De Cervantes 2",
            "Piazza A. Arrigo 4"
        )

        iconProduct = arrayOf(
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24
        )

        for(i in title.indices){

            val cart = ItemCart(iconProduct[i], title[i])
            cartArrayList.add(cart)
        }

    }
}