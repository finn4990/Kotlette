package com.Kotlette.ecommerce

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Kotlette.ecommerce.adapter.AdapterHome
import com.Kotlette.ecommerce.item.ItemHome

class HomeFragment : Fragment() {

    private lateinit var adapter : AdapterHome
    private lateinit var recyclerViewPopular : RecyclerView
    private lateinit var recyclerViewSale : RecyclerView
    private lateinit var recyclerViewAll : RecyclerView
    private lateinit var homeArrayList : ArrayList<ItemHome>

    lateinit var title: Array<String>
    lateinit var image: Array<Int>
    lateinit var price: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()

        //RecycleViewPopular
        recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular)
        recyclerViewPopular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPopular.setHasFixedSize(true)
        adapter = AdapterHome(homeArrayList)
        recyclerViewPopular.adapter = adapter

        //RecycleViewSale
        recyclerViewSale = view.findViewById(R.id.recyclerViewSale)
        recyclerViewSale.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewSale.setHasFixedSize(true)
        adapter = AdapterHome(homeArrayList)
        recyclerViewSale.adapter = adapter

        //RecycleViewAll
        recyclerViewAll = view.findViewById(R.id.recyclerViewAll)
        recyclerViewAll.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAll.setHasFixedSize(true)
        adapter = AdapterHome(homeArrayList)
        recyclerViewAll.adapter = adapter
    }

    private fun dataInitialize() {

        homeArrayList = arrayListOf<ItemHome>()

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

        image = arrayOf(
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

        price = arrayOf(
            "15,58€",
            "26,18€",
            "100,00€",
            "48,58€",
            "35,58€",
            "15,53€",
            "7,22€",
            "18,40€",
            "15,58€",
            "26,18€",
            "100,00€",
            "48,58€",
            "35,58€",
            "15,53€",
            "7,22€",
            "18,40€"
        )

        for(i in title.indices){

            val home = ItemHome(title[i], image[i], price[i])
            homeArrayList.add(home)
        }

    }

}