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
    private lateinit var recyclerView : RecyclerView
    private lateinit var homeArrayList : ArrayList<ItemHome>

    lateinit var title: Array<String>
    lateinit var image: Array<String>
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
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = AdapterHome(homeArrayList)
        recyclerView.adapter = adapter
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
            "05-05-2552",
            "20-10-2532",
            "22-05-2552",
            "29-03-2082",
            "10-01-2552",
            "20-11-2852",
            "31-11-2002",
            "20-07-2002",
            "05-05-2552",
            "20-10-2532",
            "22-05-2552",
            "29-03-2082",
            "10-01-2552",
            "20-11-2852",
            "31-11-2002",
            "20-07-2002"
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